package me.repeater64.advancedmpkeditor.backend.commands

import me.repeater64.advancedmpkeditor.backend.data_object.AllCommandsSettings
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.PracticeTypeOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.RandomiserCondition
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.util.mapIntToAlpha
import kotlin.collections.iterator

object CommandsManager {

    fun unconditionalProcessedCommand(command: String): Triple<String, CommandObjectiveRange, Set<RandomiserCondition>> {
        return Triple(command, CommandObjectiveRange("guaranteed", 0..0, 1), emptySet())
    }
    fun generateCommands(settings: AllCommandsSettings, barrelName: String) : Triple<List<String>, List<String>, Int> { // Returns commands book pages, serialization book pages, numTopLeftInvSlotsToFillLikeHotbar

        val randomisers = hashMapOf<String, Int>() // Map of randomiser identifier to total weight
        val unprocessedLabelsMap = hashMapOf<String, MutableList<Pair<CommandCondition, Set<RandomiserCondition>>>>() // Maps a label to all the states that would trigger it - each state is a pair of the processed condition so far, and a set of randomiserConditions that must also all be true

        val processedRandomSlotsCommands = mutableListOf<Triple<String, CommandObjectiveRange, Set<RandomiserCondition>>>()
        val processedCommands = mutableListOf<Triple<String, CommandObjectiveRange, Set<RandomiserCondition>>>()

        var currentRandomiserIndex = 0

        // RANDOM SLOTS (these commands will go at very start)
        val randomSlotsData = settings.randomSlotsData

        var removedBedrockIndex = 0
        for (randomSlotOptionsSet in randomSlotsData.getReorderedOptionsSets()) {
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
                        repeat(stacks) {
                            commands.add("clear @p minecraft:bedrock{a:$bedrockIndex}")
                            bedrockIndex++
                        }
                        commands.addAll(item.getGiveCommands())
                    }

                    // Now need to remove more bedrock corresponding to stacks that might have been removed if a maxStacks option were chosen
                    for (i in 0 until (maxStacks - totalActualStacks)) {
                        commands.add("clear @p minecraft:bedrock{a:$bedrockIndex}")
                        // Replace the bedrock with new bedrock to be replaced by junk at end. The template !AddedBedrockIndex! will be replaced in final processing, to ensure it only gets incremented for a command that turns out to be possible
                        commands.add("give @p minecraft:bedrock{a:!AddedBedrockIndex!}")
                        bedrockIndex++
                    }

                    return@handleWeightedList commands
                } ,
                "Warning - random slot set with no item options ($randomSlotOptionsSet)",
                randomisers, unprocessedLabelsMap, processedRandomSlotsCommands, currentRandomiserIndex)

            removedBedrockIndex += maxStacks
        }


        // FIXED SLOTS (not incl floating inv slots) (These commands will go in the main middle section)
        val fixedSlotsData = settings.fixedSlotsData
        for (fixedSlotData in fixedSlotsData.getAllSlotsExceptFloatingInvSlots()) {
            currentRandomiserIndex = handleWeightedList(fixedSlotData.itemOptions,
                { listOf(it.option.getReplaceitemCommand(fixedSlotData.minecraftSlotID)) } ,
                "Warning - fixed slot data with no item options ($fixedSlotData)",
                randomisers, unprocessedLabelsMap, processedCommands, currentRandomiserIndex)
        }

        // HEALTH+HUNGER (These commands will go in the main middle section)
        val healthHungerSettings = settings.healthHungerSettings
        processedCommands.add(unconditionalProcessedCommand("scoreboard objectives add extradmg dummy"))
        processedCommands.add(unconditionalProcessedCommand("scoreboard players set @p extradmg 0"))
        currentRandomiserIndex = handleWeightedList(healthHungerSettings.options,
            {it.option.healthOption.commands + it.option.hungerOption.commands},
            "Warning - Health hunger options has no options set", randomisers, unprocessedLabelsMap, processedCommands, currentRandomiserIndex)

        // FIRE RES (These commands will go in the main middle section)
        val fireResSettings = settings.fireResSettings
        currentRandomiserIndex = handleWeightedList(fireResSettings.options,
            {if (it.option > 0) listOf("effect give @p fire_resistance ${it.option} 0") else emptyList()},
            "Warning - Fire res settings has no options set", randomisers, unprocessedLabelsMap, processedCommands, currentRandomiserIndex)

        // FLOATING INV SLOTS (These commands will go at the end of the main middle section)
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
            processedCommands.add(unconditionalProcessedCommand("execute at @p run summon item ~ ~ ~ {Tags:[\"temp_item${tempItemIndex}\"],Item:{id:\"minecraft:stone\",Count:1b},PickupDelay:0s}"))
            processedCommands.add(unconditionalProcessedCommand("execute at @p run data modify entity @e[type=item,tag=temp_item${tempItemIndex},limit=1,sort=nearest] Item set from entity @p Inventory[{Slot:${fixedSlotData.inventoryPosition+9}b}]"))
            processedCommands.add(unconditionalProcessedCommand("replaceitem entity @p container.${topLeftInvPosition+9} minecraft:air"))
            tempItemIndex++

            currentRandomiserIndex = handleWeightedList(fixedSlotData.itemOptions,
                { listOf(it.option.getReplaceitemCommand(fixedSlotData.minecraftSlotID)) } ,
                "Warning - fixed slot data with no item options ($fixedSlotData)",
                randomisers, unprocessedLabelsMap, processedCommands, currentRandomiserIndex)

            topLeftInvPosition++
        }


        val randomSlotCommands = mutableListOf<String>()
        val mainCommands = mutableListOf<String>()

        // Attempt to process unprocessedLabelsMap
        val processedLabelsMap = hashMapOf<String, CommandCondition>() // Maps a randomiser label to the full condition

        var n = 0
        while (true) {
            // Look for unprocessed labels that depend on a processed label, so can be partially resolved
            for ((_, pairs) in unprocessedLabelsMap) {
                for ((index, pair) in pairs.withIndex()) {
                    for (dependentCondition in pair.second) {
                        if (processedLabelsMap.containsKey(dependentCondition.conditionLabel)) {
                            val setWithNewlyResolvedConditionRemoved = pair.second.toMutableSet()
                            setWithNewlyResolvedConditionRemoved.remove(dependentCondition)

                            // Get the processed condition and invert if needed
                            var alreadyProcessedCondition = processedLabelsMap[dependentCondition.conditionLabel]!!
                            if (dependentCondition.isInverted) {
                                alreadyProcessedCondition = !alreadyProcessedCondition
                            }

                            // Need to AND the processed condition with the currently stored condition
                            pairs[index] = Pair(pair.first and alreadyProcessedCondition, setWithNewlyResolvedConditionRemoved)

                            break // Stop going through conditions now since we've mutated data, anything else left to be resolved we'll catch on the next iteration of the outermost while loop
                        }
                    }
                }
            }

            // Look for unprocessed labels that are paired with emptySet, ie can be resolved trivially
            val labelsToRemove = mutableListOf<String>()
            for ((label, pairs) in unprocessedLabelsMap) {
                val pairsToRemove = mutableListOf<Pair<CommandCondition, Set<RandomiserCondition>>>()
                for (pair in pairs) {
                    if (pair.second.isEmpty()) {
                        if (processedLabelsMap.contains(label)) {
                            // Something for this label is already processed, so we need to OR the conditions
                            processedLabelsMap[label] = processedLabelsMap[label]!! or pair.first
                        } else {
                            processedLabelsMap[label] = pair.first
                        }
                        pairsToRemove.add(pair)
                    }
                }
                pairs.removeAll(pairsToRemove) // Remove from unprocessed map
                if (pairs.isEmpty()) {
                    labelsToRemove.add(label) // Can clear up whole thing
                }
            }
            labelsToRemove.forEach { unprocessedLabelsMap.remove(it) }


            if (unprocessedLabelsMap.isEmpty()) break
            n++
            if (n > 50) {
                // We almost certainly have circular condition dependencies.
                throw CircularConditionsException(barrelName, unprocessedLabelsMap.keys.joinToString(", "))
            }
        }


        // Add all commands
        var addedBedrockIndex = 27
        val actuallyUsedRandomisers: MutableSet<String> = hashSetOf() // Set of randomiser objectives
        for ((index, triple) in (processedRandomSlotsCommands + processedCommands).withIndex()) {
            val (command, objectiveRange, conditionSet) = triple
            val mainSection = index >= processedRandomSlotsCommands.size
            val commandsListToAddTo = if (mainSection) mainCommands else randomSlotCommands

            if (command.endsWith("intentionally_invalid_command")) {
                // This command won't do anything, skip it
                continue
            }

            var overallCondition: CommandCondition = objectiveRange
            for (randomiserCondition in conditionSet) {
                var logicalCondition = processedLabelsMap[randomiserCondition.conditionLabel]!!
                if (randomiserCondition.isInverted) logicalCondition = !logicalCondition

                overallCondition = overallCondition and logicalCondition
            }

            val actualCommand = if (overallCondition.getOrListOfAnds().isNotEmpty() && command.contains("!AddedBedrockIndex!")) {
                command.replace("!AddedBedrockIndex!", (addedBedrockIndex++).toString())
            } else {
                command
            }

            for (andList in overallCondition.getOrListOfAnds()) {
                // Remove any guaranteed conditions
                val cleanedUpAndList = andList.filterNot { it.range == (0 until it.totalWeight) }

                if (cleanedUpAndList.isEmpty()) {
                    // Command is fully unconditional, add it directly without any /execute
                    commandsListToAddTo.add(actualCommand)
                } else {
                    val sb = StringBuilder("execute ")
                    for (condition in cleanedUpAndList) {
                        sb.append(condition.getCommandIfString())
                        sb.append(" ")
                        actuallyUsedRandomisers.add(condition.objective)
                    }
                    sb.append("run $actualCommand")
                    commandsListToAddTo.add(sb.toString())
                }
            }
        }

        // JUNK
        val junkCommands = mutableListOf<String>()
        val junkSettings = settings.junkSettings
        if (junkSettings.enableJunk) {
            val actualJunkList = junkSettings.getActualJunkList()
            // Create scoreboard objective to store clear command result
            junkCommands.add("scoreboard objectives add clear dummy")

            var i = 0
            for (bedrockIndex in 0..addedBedrockIndex) {
                val junkItem = actualJunkList[i % actualJunkList.size]

                junkCommands.add("execute store result score @p clear run clear @p minecraft:bedrock{a:$bedrockIndex}")
                junkCommands.addAll(junkItem.getGiveCommands(if (junkSettings.makeJunkNonStackable) i else null).map { "execute unless score @p clear matches 0 run $it" })

                i++
            }
        }


        val initialSetupCommands = mutableListOf<String>()
        initialSetupCommands.add("tellraw @p [{\"text\":\"MPK Setup Created With \",\"color\":\"aqua\"},{\"text\":\"repeater64.github.io/AdvancedMpkEditor\",\"bold\":true,\"color\":\"dark_aqua\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://repeater64.github.io/AdvancedMpkEditor/\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Click to open editor website!\"}}]")

        // Potential stronghold portal flag
        initialSetupCommands.add("scoreboard objectives add shportal dummy")
        if (settings.practiceTypeOption == PracticeTypeOption.STRONGHOLD) {
            initialSetupCommands.add("scoreboard players set @p shportal 1")
        } else {
            initialSetupCommands.add("scoreboard players set @p shportal 0")
        }

        // Generate randomiser commands
        for (randomiserIdentifier in actuallyUsedRandomisers) {
            val totalWeight = randomisers[randomiserIdentifier]!!
            if (totalWeight == 1) continue // Not needed
            initialSetupCommands.add("scoreboard objectives add $randomiserIdentifier dummy")
            initialSetupCommands.add("scoreboard players set !m $randomiserIdentifier $totalWeight")
            initialSetupCommands.add("execute store result score @p $randomiserIdentifier run data get entity @e[limit=1,sort=random] UUID[0]")
            initialSetupCommands.add("scoreboard players operation @p $randomiserIdentifier %= !m $randomiserIdentifier")
        }


        return Triple((initialSetupCommands + randomSlotCommands + junkCommands + mainCommands), AllCommandsSettings.serializeToPages(settings), fixedSlotsData.numInvSlotsWithItems())
    }

    private fun <T> handleWeightedList(options: WeightedOptionList<T>, commandsGetter: (WeightedOption<T>) -> List<String>, ifEmptyMessage: String,
                                       randomisers: HashMap<String, Int>,
                                       unprocessedLabelsMap: HashMap<String, MutableList<Pair<CommandCondition, Set<RandomiserCondition>>>>,
                                       processedCommands: MutableList<Triple<String, CommandObjectiveRange, Set<RandomiserCondition>>>,
                                       initialRandomiserIndex: Int) : Int { // Returns new currentRandomiserIndex

        var currentRandomiserIndex = initialRandomiserIndex

        if (options.options.isEmpty()) {
            println(ifEmptyMessage)
            return currentRandomiserIndex
        }

        // Handle simple case where there is only one option. In this case, no labels or randomiser links are allowed so they won't be considered
        if (options.options.size == 1) {
            for (command in commandsGetter(options.options[0])) {
                processedCommands.add(unconditionalProcessedCommand(command))
            }
            return currentRandomiserIndex
        }

        val allConditionLists = options.generateAllConditionLists()

        for ((conditionSet, weightedList) in allConditionLists) {
            val randomiserIdentifier = mapIntToAlpha(currentRandomiserIndex)
            currentRandomiserIndex++
            randomisers[randomiserIdentifier] = weightedList.totalWeight

            var weightSoFar = 0
            for (option in weightedList.options) {
                val weightRange = weightSoFar..<weightSoFar+option.weight
                weightSoFar += option.weight

                val commandCondition = CommandObjectiveRange(randomiserIdentifier, weightRange, weightedList.totalWeight)
                for (command in commandsGetter(option)) {
                    processedCommands.add(Triple(command, commandCondition, conditionSet.toSet()))
                }

                if (option.label != null) {
                    unprocessedLabelsMap.getOrPut(option.label!!) {mutableListOf()}.add(Pair(commandCondition, conditionSet))
                }
            }
        }

        return currentRandomiserIndex
    }

    fun loadSettings(serializedPages: List<String>) : AllCommandsSettings {
        return AllCommandsSettings.Companion.deserializeFromPages(serializedPages)
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