package me.repeater64.advancedmpkeditor.gui.screens.barrel

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowOverflow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import me.repeater64.advancedmpkeditor.backend.data_object.item.DontReplaceMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.ForcedEmptyMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.random_slot.RandomSlotOptionsSet
import me.repeater64.advancedmpkeditor.backend.data_object.random_slot.RandomSlotsData
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.backend.presets_examples.availableItem
import me.repeater64.advancedmpkeditor.gui.component.MinecraftSlotDisplay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ColumnScope.RandomSlotsEditor(
    randomSlotsData: RandomSlotsData,
    allLabels: MutableSet<String>,
    showDialogCallback: (@Composable () -> Any) -> Any,
    hideDialogCallback: () -> Unit
) {
    Text("Random Slot Items", style = MaterialTheme.typography.headlineLarge)
    Spacer(Modifier.height(15.dp))

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        for (optionSet in randomSlotsData.optionsSets) {
            RandomSlotsRow(randomSlotsData, optionSet, allLabels, showDialogCallback, hideDialogCallback)
        }

        // Add new
        val tooltipState = rememberTooltipState()
        TooltipBox(
            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
            tooltip = {
                PlainTooltip() { Text(text = "Click to add!") }
            },
            state = tooltipState,
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(horizontal = 50.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = (-30).dp,
                shadowElevation = 4.dp,
                onClick = {
                    val newOptionsSet = RandomSlotOptionsSet("Unnamed", WeightedOptionList(mutableListOf(WeightedOption(listOf(DontReplaceMinecraftItem(true)).toMutableStateList()))))
                    randomSlotsData.optionsSets.add(newOptionsSet)
                    showDialogCallback { RandomSlotPopup(newOptionsSet, allLabels, hideDialogCallback, deleteCallback = {randomSlotsData.optionsSets.remove(newOptionsSet)}) }
                },
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun ColumnScope.RandomSlotsRow(
    data: RandomSlotsData,
    optionsSet: RandomSlotOptionsSet,
    allLabels: MutableSet<String>,
    showDialogCallback: (@Composable () -> Any) -> Any,
    hideDialogCallback: () -> Unit
) {
    val tooltipState = rememberTooltipState()
    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
        tooltip = {
            PlainTooltip() { Text(text = "Click to edit!") }
        },
        state = tooltipState,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(horizontal = 50.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = (-30).dp,
            shadowElevation = 4.dp,
            onClick = {
                showDialogCallback { RandomSlotPopup(optionsSet, allLabels, hideDialogCallback, deleteCallback = {data.optionsSets.remove(optionsSet)}) }
            },
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(optionsSet.setName, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(start=20.dp, top=5.dp))


                FlowRow(
                    maxLines = 1,
                    itemVerticalAlignment = Alignment.CenterVertically,
                    overflow = FlowRowOverflow.expandIndicator {
                        Text(" ...", style = MaterialTheme.typography.bodyLarge)
                    },
                ) {
                    for ((index, option) in optionsSet.options.options.withIndex()) {
                        Surface(
                            modifier = Modifier
                                .padding(horizontal = 15.dp),
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 20.dp,
                            shadowElevation = 6.dp,
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                for (item in option.option) {
                                    MinecraftSlotDisplay(item, 50, modifier=Modifier.padding(horizontal = 10.dp)).ContentsOnly()
                                }
                            }
                        }
                        if (index != optionsSet.options.options.size-1) {
                            Text("or", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}