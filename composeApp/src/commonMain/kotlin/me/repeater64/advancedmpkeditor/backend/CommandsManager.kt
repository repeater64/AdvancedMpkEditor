package me.repeater64.advancedmpkeditor.backend

import me.repeater64.advancedmpkeditor.backend.data_object.AllCommandsSettings
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.*
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.RandomiserCondition
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
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
            if (fixedSlotData.itemOptions.options.isEmpty()) {
                println("Warning - fixed slot data with no item options ($fixedSlotData)")
                continue
            }

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
            ifConditionsSet.forEach { ifCondition -> info.addAll((randomiserConditionsMap[ifCondition] ?: emptyList()).map { "${it.replace("if ", "")}!${ifCondition.conditionLabel}" }) }
            unlessConditionsSet.forEach { ifCondition -> info.addAll((randomiserConditionsMap[ifCondition] ?: emptyList()).map { "${it.replace("unless ", "")}!${ifCondition.conditionLabel}" }) }

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
        // Parse info data to load label names
        val labelNames = hashMapOf<String, String>() // Map of condition (the condition minus the if or unless) to the label
        for (infoLine in info) {
            val split = infoLine.split("!")
            labelNames[split[0]] = split[1]
        }



        val fixedSlotsData = hashMapOf<String, FixedSlotData>() // Map of the slotString to the FixedSlotData we are building up as we parse the commands
        val alreadySeenFixedSlotOptions = hashMapOf<String, MutableSet<Pair<MinecraftItem, Set<RandomiserCondition>>>>() // Map of slotString to a set of pairs of (the item for that option, the conditions)

        // Parse commands
        commandloop@ for (command in commands) {
            if (command.startsWith("scoreboard") || command.startsWith("execute store")) {
                // This is a randomiser setup, which we don't need to process here
                continue@commandloop
            }

            // Fixed Slots
            if (command.contains("replaceitem")) {

                val commandTail = command.split("eplaceitem entity @p ")[1] // Intentionally removed the "r", so that if this is a fully unconditional command that starts with replaceitem the split still works the same
                val slotString = commandTail.split(" ")[0]
                val itemString = commandTail.removePrefix("$slotString ")
                val item = MinecraftItem.fromCommandString(itemString)

                val slotData = fixedSlotsData.getOrPut(slotString) { FixedSlotData.createEmpty(slotString) }

                val weight: Int
                var label: String? = null
                val conditions: MutableList<RandomiserCondition> = mutableListOf()

                val conditionStrings = extractConditions(command)

                // Work out the weight by processing the first condition string (and also check for label)
                val firstConditionIsLink: Boolean
                weight = if (conditionStrings.isNotEmpty()) {
                    val firstConditionString = conditionStrings.first()
                    val firstConditionStringLabelLookup = firstConditionString.replace("if ", "").replace("unless ", "")

                    if (!firstConditionString.contains("unless") && (!labelNames.containsKey(firstConditionStringLabelLookup)) || conditionStrings.size == 1) {
                        // This first condition is an if, and either has no label associated with it or is the only condition.
                        // This means that this is the primary condition that tells us the weight, not a linked condition
                        firstConditionIsLink = false

                        if (labelNames.containsKey(firstConditionStringLabelLookup)) {
                            // There is only one condition and it is labelled, so this is a labelled option.
                            label = labelNames[firstConditionStringLabelLookup]
                        }

                        // Work out the weight
                        if (firstConditionString.contains("..")) {
                            (firstConditionString.split("..")[1].toInt() - firstConditionString.split("..")[0].split("matches ")[1].toInt()) + 1
                        } else {
                            1
                        }
                    } else {
                        // This first condition is a linked condition. This means we skipped over the randomiser directly associated with this option, ie there was only one option and it had a weight of 1
                        firstConditionIsLink = true
                        1
                    }
                } else {
                    // There are no conditions at all
                    firstConditionIsLink = false
                    1
                }

                val linkedConditionStrings = if (!firstConditionIsLink) conditionStrings.drop(1) else conditionStrings

                for (linkedConditionString in linkedConditionStrings) {
                    val linkedConditionStringLabelLookup = linkedConditionString.replace("if ", "").replace("unless ", "")
                    if (!labelNames.containsKey(linkedConditionStringLabelLookup)) {
                        println("Error - found what seems to be a linked condition, but no lookup label stored! Skipping processing this command ($command)")
                        continue@commandloop
                    }

                    conditions.add(RandomiserCondition(labelNames[linkedConditionStringLabelLookup]!!, linkedConditionString.contains("unless")))
                }

                // Need to check for duplicates (ie a single option that got split into multiple commands because there are multiple score conditions that trigger the same labels)
                val optionPair = Pair(item, conditions.toSet())
                if (!alreadySeenFixedSlotOptions.getOrPut(slotString) { mutableSetOf() }.contains(optionPair)) {
                    alreadySeenFixedSlotOptions[slotString]!!.add(optionPair)
                    // This is a unique option, add it to the slot data
                    slotData.itemOptions.options.add(WeightedOption(item, weight, label, conditions))
                }

            }
        }

        // Build up fixedSlotsData
        // Start with empty defaults in case any weren't specified
        var offhandSlotData = OffhandSlotData.empty()
        var hotbarSlotsData = Array(9) { HotbarSlotData.empty(it) }
        var inventorySlotsData = Array(27) { InventorySlotData.empty(it) }
        var helmetSlotData = HelmetSlotData.empty()
        var chestplateSlotData = ChestplateSlotData.empty()
        var leggingsSlotData = LeggingsSlotData.empty()
        var bootsSlotData = BootsSlotData.empty()

        for (slotData in fixedSlotsData.values) {
            when (slotData) {
                is OffhandSlotData -> offhandSlotData = slotData
                is HotbarSlotData -> hotbarSlotsData[slotData.hotbarPosition] = slotData
                is InventorySlotData -> inventorySlotsData[slotData.inventoryPosition] = slotData
                is HelmetSlotData -> helmetSlotData = slotData
                is ChestplateSlotData -> chestplateSlotData = slotData
                is LeggingsSlotData -> leggingsSlotData = slotData
                is BootsSlotData -> bootsSlotData = slotData
            }
        }

        val fixedSlotsDataFinal = FixedSlotsData(offhandSlotData, hotbarSlotsData, inventorySlotsData, helmetSlotData, chestplateSlotData, leggingsSlotData, bootsSlotData)

        return AllCommandsSettings(fixedSlotsDataFinal)
    }

    private fun extractConditions(command: String): List<String> {
        // This regex looks for "if" or "unless" followed by everything
        // until it hits another "if", "unless", or "run".
        val regex = """(if|unless)\s+(.*?)(?=\s+(if|unless|run))""".toRegex()

        return regex.findAll(command).map { it.value.trim() }.toList()
    }

    private fun <T> cartesianProduct(lists: List<List<T>>): List<List<T>> {
        if (lists.isEmpty()) return listOf(emptyList())
        return lists.fold(listOf(listOf<T>())) { acc, list ->
            acc.flatMap { prefix -> list.map { element -> prefix + element } }
        }
    }
}