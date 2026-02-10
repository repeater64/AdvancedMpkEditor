package me.repeater64.advancedmpkeditor.backend

import me.repeater64.advancedmpkeditor.backend.data_object.AllCommandsSettings
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.*
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.random_slot.RandomSlotOptionsSet
import me.repeater64.advancedmpkeditor.backend.data_object.random_slot.RandomSlotsData
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.RandomiserCondition
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.util.mapIntToAlpha
import me.repeater64.advancedmpkeditor.util.prettyPrintDataClass
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator

object CommandsManager {

    fun generateCommands(settings: AllCommandsSettings) : Triple<List<String>, List<String>, Int> { // Returns commands book pages, serialization book pages, numTopLeftInvSlotsToFillLikeHotbar

        val randomisers = hashMapOf<String, Int>() // Map of randomiser identifier to total weight
        val randomiserConditionsMap = hashMapOf<RandomiserCondition, MutableList<String>>() // Maps a randomiser condition to all the "if ...." and "unless ..." strings that would trigger it
        val processedCommands = mutableListOf<Pair<String, Set<RandomiserCondition>>>() // If associated with empty set, these commands need no further modification. Otherwise, the key is the command with the template "CONDITION" to be replaced.
        val processedCommandsAtEnd = mutableListOf<Pair<String, Set<RandomiserCondition>>>() // Same as above but will be executed after all the processedCommands
        var currentRandomiserIndex = 0


        // FIXED SLOTS (not incl floating inv slots)
        val fixedSlotsData = settings.fixedSlotsData
        for (fixedSlotData in fixedSlotsData.getAllSlotsExceptFloatingInvSlots()) {
            currentRandomiserIndex = handleWeightedList(fixedSlotData.itemOptions,
                { listOf("replaceitem entity @p ${fixedSlotData.minecraftSlotID} ${it.option.commandString}") } ,
                "Warning - fixed slot data with no item options ($fixedSlotData)",
                randomisers, randomiserConditionsMap, processedCommands, currentRandomiserIndex)
        }

        // FLOATING INV SLOTS
        var tempItemIndex = 0
        var topLeftInvPosition = 0
        for (fixedSlotData in fixedSlotsData.getFloatingInvSlots()) {

            while (topLeftInvPosition in fixedSlotsData.getNonFloatingInvSlotInventoryPositions()) {
                topLeftInvPosition++
            }

            // This bit is pretty weird. The way we handle this, broadly speaking, is to move the (random) item currently in the target slot to the top left of the inventory,
            // where it will be replacing a barrier (put there by HotbarFillingChest). Then we can safely /replaceitem the target slot with the wanted item.
            // The way we actually do this is to spawn an item at the player's location, modify its data so that it's a copy of the item in the target slot, then
            // clear the top left slot that we want to put it in, so that it gets picked up by the player the next tick in that slot. (we can also /replaceitem the target slot as soon as we've copied the data away)

            // These first 3 commands will be the same regardless of which option is chosen, can add them as unconditional commands
            processedCommandsAtEnd.add(Pair("execute at @p run summon item ~ ~ ~ {Tags:[\"temp_item${tempItemIndex}\"],Item:{id:\"minecraft:stone\",Count:1b},PickupDelay:0s}", emptySet()))
            processedCommandsAtEnd.add(Pair("execute at @p run data modify entity @e[type=item,tag=temp_item${tempItemIndex},limit=1,sort=nearest] Item set from entity @p Inventory[{Slot:${fixedSlotData.inventoryPosition+9}b}]", emptySet()))
            processedCommandsAtEnd.add(Pair("replaceitem entity @p container.${topLeftInvPosition+9} minecraft:air", emptySet()))
            tempItemIndex++

            currentRandomiserIndex = handleWeightedList(fixedSlotData.itemOptions,
                { listOf("replaceitem entity @p ${fixedSlotData.minecraftSlotID} ${it.option.commandString}") } ,
                "Warning - fixed slot data with no item options ($fixedSlotData)",
                randomisers, randomiserConditionsMap, processedCommandsAtEnd, currentRandomiserIndex) // Note we pass in processedCommandsAtEnd instead of processedCommands so we get these executing at the end

            topLeftInvPosition++
        }

        // RANDOM SLOTS
        val randomSlotsData = settings.randomSlotsData

        var removedBedrockIndex = 0
        var addedBedrockIndex = 27
        for (randomSlotOptionsSet in randomSlotsData.optionsSets) {
            val maxStacks = randomSlotOptionsSet.maxNumStacks

            currentRandomiserIndex = handleWeightedList(randomSlotOptionsSet.options,
                {
                    val itemsList = it.option
                    val commands = mutableListOf<String>()

                    var bedrockIndex = removedBedrockIndex

                    var totalActualStacks = 0
                    for (item in itemsList) {
                        val stacks = item.numStacks
                        totalActualStacks += stacks
                        for (i in 0 until stacks) {
                            commands.add("clear @p minecraft:bedrock{a:$bedrockIndex}")
                            bedrockIndex++
                        }
                        commands.add("give @p ${item.commandString}")
                    }

                    // Now need to remove more bedrock corresponding to stacks that might have been removed if a maxStacks option were chosen
                    for (i in 0 until (maxStacks - totalActualStacks)) {
                        commands.add("clear @p minecraft:bedrock{a:$bedrockIndex}")
                        // Replace the bedrock with new bedrock to be replaced by junk at end
                        commands.add("give @p minecraft:bedrock{a:$addedBedrockIndex}")
                        bedrockIndex++
                        addedBedrockIndex++
                    }

                    return@handleWeightedList commands
                } ,
                "Warning - random slot set with no item options ($randomSlotOptionsSet)",
                randomisers, randomiserConditionsMap, processedCommands, currentRandomiserIndex)

            removedBedrockIndex += maxStacks
        }

        // JUNK
        val junkSettings = settings.junkSettings
        if (junkSettings.enableJunk) {
            // Create scoreboard objective to store clear command result
            processedCommands.add(Pair("scoreboard objectives add clear dummy", emptySet()))

            var i = 0
            for (bedrockIndex in 0..addedBedrockIndex) {
                val junkItem = junkSettings.junkList[i % junkSettings.junkList.size]

                processedCommands.add(Pair("execute store result score @p clear run clear @p minecraft:bedrock{a:$bedrockIndex}", emptySet()))
                processedCommands.add(Pair("execute unless score @p clear matches 0 run give @p ${if (junkSettings.makeJunkNonStackable) junkItem.getCommandStringNonStackable(i) else junkItem.commandString}", emptySet()))

                i++
            }
        }

        // HEALTH+HUNGER
        val healthHungerSettings = settings.healthHungerSettings
        processedCommands.add(Pair("scoreboard objectives add extradmg dummy", emptySet()))
        processedCommands.add(Pair("scoreboard players set @p extradmg 0", emptySet()))
        currentRandomiserIndex = handleWeightedList(healthHungerSettings.options,
            {it.option.healthOption.commands + it.option.hungerOption.commands},
            "Warning - Health hunger options has no options set", randomisers, randomiserConditionsMap, processedCommands, currentRandomiserIndex)


        val commands = mutableListOf<String>()

        commands.add("say TODO plug this tool, put the URL (clickable) and nice format") // TODO (also maybe make it a tellraw so it doesn't start with [MPK]

        // Generate randomiser commands
        for ((randomiserIdentifier, totalWeight) in randomisers) {
            if (totalWeight == 1) continue // Not needed, and this should have been detected when adding the conditions earlier
            commands.add("scoreboard objectives add $randomiserIdentifier dummy")
            commands.add("scoreboard players set !m $randomiserIdentifier $totalWeight")
            commands.add("execute store result score @p $randomiserIdentifier run data get entity @e[limit=1,sort=random] UUID[0]")
            commands.add("scoreboard players operation @p $randomiserIdentifier %= !m $randomiserIdentifier")
        }


        // Add all commands
        for ((command, conditionSet) in (processedCommands + processedCommandsAtEnd)) {
            if (command.endsWith("intentionally_invalid_command")) {
                // This command won't do anything, skip it
                continue
            }
            if (conditionSet.isEmpty()) {
                // Unconditional command, can just add
                commands.add(command)
                continue
            }

            val ifConditionsSet = conditionSet.filter { !it.isInverted }
            val unlessConditionsSet = conditionSet.filter { it.isInverted }

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
                val finalCommand = command.replace("CONDITION", conditionString)

                // Process the final command to look for unnecessary conditions (ie guaranteed to be true) that could be removed
                commands.add(simplifyCommand(finalCommand))
            }
        }


        return Triple(commands, AllCommandsSettings.serializeToPages(settings), fixedSlotsData.numInvSlotsWithItems())
    }

    fun simplifyCommand(command: String): String {
        // TODO also look for adjacent score ranges that could be merged

        // Separate the conditions from the rest of the command
        val runMatch = Regex("""\srun\s""").find(command) ?: return command
        val runIndex = runMatch.range.first

        val conditionsPart = command.substring(0, runIndex).removePrefix("execute ").trim()
        val actionPart = command.substring(runIndex).trim()

        // Define the pattern for score conditions
        // Groups: 1 = type (if/unless), 2 = objective name, 3 = the full condition string
        val conditionRegex = Regex("""((if|unless) score @p (\w+) matches [\d\.\-]+)""")
        val matches = conditionRegex.findAll(conditionsPart).toList()

        // Identify "if" conditions
        val objectivesWithIf = matches
            .filter { it.groupValues[2] == "if" }
            .map { it.groupValues[3] } // The objective name (e.g., "h")
            .toSet()

        // Filter out "unless" conditions if the same objective is already in our "if" set
        val simplifiedConditions = matches.filterNot { match ->
            val type = match.groupValues[2]
            val objective = match.groupValues[3]
            type == "unless" && objectivesWithIf.contains(objective)
        }.joinToString(" ") { it.groupValues[1] }


        return "execute $simplifiedConditions $actionPart".replace(Regex("\\s+"), " ")
    }

    private fun <T> handleWeightedList(options: WeightedOptionList<T>, commandsGetter: (WeightedOption<T>) -> List<String>, ifEmptyMessage: String,
                                       randomisers: HashMap<String, Int>,
                                       randomiserConditionsMap: HashMap<RandomiserCondition, MutableList<String>>,
                                       processedCommands: MutableList<Pair<String, Set<RandomiserCondition>>>,
                                       initialRandomiserIndex: Int) : Int { // Returns new currentRandomiserIndex

        var currentRandomiserIndex = initialRandomiserIndex

        if (options.options.isEmpty()) {
            println(ifEmptyMessage)
            return currentRandomiserIndex
        }

        // Handle simple case where there is only one option. In this case, no labels or randomiser links are allowed so they won't be considered
        if (options.options.size == 1) {
            for (command in commandsGetter(options.options[0])) {
                processedCommands.add(Pair(command, emptySet()))
            }
            return currentRandomiserIndex
        }

        val allConditionLists = options.generateAllConditionLists()

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
                    for (command in commandsGetter(option)) {
                        commandsWithTemplateToAdd.add("execute CONDITION run $command")
                    }
                } else {
                    for (command in commandsGetter(option)) {
                        commandsWithTemplateToAdd.add("execute if score @p $randomiserIdentifier matches $weightRange CONDITION run $command")
                    }
                }

                if (option.label != null) {
                    val positiveCondition = RandomiserCondition(option.label, false)
                    val negativeCondition = RandomiserCondition(option.label, true)

                    randomiserConditionsMap.getOrPut(positiveCondition) { mutableListOf() }.add("if score @p $randomiserIdentifier matches $weightRange")
                    randomiserConditionsMap.getOrPut(negativeCondition) { mutableListOf() }.add("unless score @p $randomiserIdentifier matches $weightRange")
                }
            }

            if (conditionSet.isEmpty()) {
                processedCommands.addAll(commandsWithTemplateToAdd.map { it.replace("CONDITION ", "") }.map { Pair(it, emptySet()) })
            } else {
                processedCommands.addAll(commandsWithTemplateToAdd.map { Pair(it, conditionSet) })
            }
        }

        return currentRandomiserIndex
    }

    fun loadSettings(serializedPages: List<String>) : AllCommandsSettings {
        return AllCommandsSettings.deserializeFromPages(serializedPages)
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