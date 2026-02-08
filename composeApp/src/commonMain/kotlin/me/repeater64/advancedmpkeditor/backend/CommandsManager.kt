package me.repeater64.advancedmpkeditor.backend

import me.repeater64.advancedmpkeditor.backend.data_object.AllCommandsSettings
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.*
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.RandomiserCondition
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.util.mapIntToAlpha

object CommandsManager {
    fun generateCommands(settings: AllCommandsSettings) : Pair<List<String>, List<String>> { // Returns commands book pages + info book pages

        val randomisers = hashMapOf<String, Int>() // Map of randomiser identifier to total weight
        val randomiserConditionsMap = hashMapOf<RandomiserCondition, MutableList<String>>() // Maps a randomiser condition to all the "if ...." and "unless ..." strings that would trigger it
        val unconditionalCommands = mutableListOf<String>() // Commands that won't need any modification later since they don't depend on linked conditions
        val conditionalCommands = mutableListOf<Pair<String, Set<RandomiserCondition>>>() // Commands that will need modification after first pass. String is the command with the template "CONDITION" to be replaced.
        var currentRandomiserIndex = 0


        // FIXED SLOTS
        val fixedSlotsData = settings.fixedSlotsData

        for (fixedSlotData in fixedSlotsData.allSlots()) {
            if (fixedSlotData.forcedEmpty) {
                unconditionalCommands.add("replaceitem entity @p ${fixedSlotData.minecraftSlotID} minecraft:air")
            } else if (fixedSlotData.itemOptions.options.isNotEmpty()) {
                // Handle simple case where there is only one option. In this case, no labels or randomiser links are allowed so they won't be considered
                if (fixedSlotData.itemOptions.options.size == 1) {
                    unconditionalCommands.add("replaceitem entity @p ${fixedSlotData.minecraftSlotID} ${fixedSlotData.itemOptions.options[0].option.commandString}")
                    continue
                }

                val allConditionLists = fixedSlotData.itemOptions.generateAllConditionLists()

                for ((conditionSet, weightedList) in allConditionLists) {
                    val randomiserIdentifier = mapIntToAlpha(currentRandomiserIndex)
                    currentRandomiserIndex++
                    randomisers[randomiserIdentifier] = weightedList.totalWeight

                    val commandsWithTemplateToAdd = mutableListOf<String>()

                    var weightSoFar = 0
                    for (option in weightedList.options) {
                        val weightRange = if (option.weight == 1) weightSoFar.toString() else "${weightSoFar}..${weightSoFar+option.weight-1}"
                        weightSoFar += option.weight

                        if (weightedList.totalWeight == 1) {
                            // This is a case where for this set of conditions, this option is the only option.
                            commandsWithTemplateToAdd.add("execute CONDITION run replaceitem entity @p ${fixedSlotData.minecraftSlotID} ${option.option.commandString}")
                        } else {
                            commandsWithTemplateToAdd.add("execute if score @p $randomiserIdentifier matches $weightRange CONDITION run replaceitem entity @p ${fixedSlotData.minecraftSlotID} ${option.option.commandString}")
                        }

                        if (option.label != null) {
                            val positiveCondition = RandomiserCondition(option.label, false)
                            val negativeCondition = RandomiserCondition(option.label, true)

                            randomiserConditionsMap.getOrPut(positiveCondition) { mutableListOf() }.add("if score @p $randomiserIdentifier matches $weightRange")
                            randomiserConditionsMap.getOrPut(negativeCondition) { mutableListOf() }.add("unless score @p $randomiserIdentifier matches $weightRange")
                        }
                    }

                    if (conditionSet.isEmpty()) {
                        unconditionalCommands.addAll(commandsWithTemplateToAdd.map { it.replace("CONDITION ", "") })
                    } else {
                        conditionalCommands.addAll(commandsWithTemplateToAdd.map { Pair(it, conditionSet) })
                    }
                }
            }
        }

        // TODO other types of stuff


        val commands = mutableListOf<String>()
        val info = mutableSetOf<String>()

        // Generate randomiser commands
        for ((randomiserIdentifier, totalWeight) in randomisers) {
            if (totalWeight == 1) continue // Not needed, and this should have been detected when adding the conditions earlier
            commands.add("scoreboard objectives add $randomiserIdentifier dummy")
            commands.add("scoreboard players set !m $randomiserIdentifier $totalWeight")
            commands.add("execute store result score @p $randomiserIdentifier run data get entity @e[limit=1,sort=random] UUID[0]")
            commands.add("scoreboard players operation @p $randomiserIdentifier %= !m $randomiserIdentifier")
        }

        // Add all unconditional commands
        commands.addAll(unconditionalCommands)

        // Add all conditional commands
        for ((command, conditionSet) in conditionalCommands) {
            val ifConditionsSet = conditionSet.filter { !it.isInverted }
            val unlessConditionsSet = conditionSet.filter { it.isInverted }

            // Add labels to info (needed to be able to work out the labels from hotbar.nbt)
            ifConditionsSet.forEach { ifCondition -> info.addAll((randomiserConditionsMap[ifCondition] ?: emptyList()).map { "${it.replace("if", "")}!${ifCondition.conditionLabel}" }) }
            unlessConditionsSet.forEach { ifCondition -> info.addAll((randomiserConditionsMap[ifCondition] ?: emptyList()).map { "${it.replace("unless", "")}!${ifCondition.conditionLabel}" }) }

            // Get the list of strings for every if condition in the set
            val listsToCombine = ifConditionsSet.map { randomiserConditionsMap[it] ?: emptyList() }

            // If any condition has no valid strings, the command is impossible to satisfy
            if (listsToCombine.any { it.isEmpty() }) {
                println("Warning - found a never-triggered condition (one of the set $ifConditionsSet) (used for command $command)")
                continue
            }

            val ifConditionCombinations = cartesianProduct(listsToCombine)

            // Combine all of the unless condition strings together
            val allUnlessConditionStrings = unlessConditionsSet.fold(mutableSetOf<String>()) {acc, it -> acc.addAll(randomiserConditionsMap[it] ?: emptyList()); acc }

            for (combination in ifConditionCombinations) {
                // Always add in ALL of the unless conditions to everything
                val conditionString = (combination + allUnlessConditionStrings).joinToString(" ")
                commands.add(command.replace("CONDITION", conditionString))
            }
        }

        commands.add("clear @p minecraft:barrier")
        commands.add("clear @p minecraft:bedrock")


        return Pair(commands, info.toList())
    }

    fun loadSettings(commands: List<String>, info: List<String>) : AllCommandsSettings {
        // TODO temporary, returns blank one
        val opt = WeightedOptionList<MinecraftItem>(mutableListOf())
        val emptyFixedSlotsData = FixedSlotsData(OffhandSlotData(opt), Array(0) { HotbarSlotData(0, opt) }, Array<InventorySlotData>(0) { InventorySlotData(0, false, opt) }, HelmetSlotData(opt), ChestplateSlotData(opt), LeggingsSlotData(opt), BootsSlotData(opt))
        return AllCommandsSettings(emptyFixedSlotsData)
    }

    private fun <T> cartesianProduct(lists: List<List<T>>): List<List<T>> {
        if (lists.isEmpty()) return listOf(emptyList())
        return lists.fold(listOf(listOf<T>())) { acc, list ->
            acc.flatMap { prefix -> list.map { element -> prefix + element } }
        }
    }
}