package me.repeater64.advancedmpkeditor.gui.screens.barrel.random_slot

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.repeater64.advancedmpkeditor.backend.data_object.item.DontReplaceMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.random_slot.RandomSlotOptionsSet
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionEitherType
import me.repeater64.advancedmpkeditor.backend.presets_examples.presets.RandomSlotPreset
import me.repeater64.advancedmpkeditor.backend.presets_examples.presets.RandomSlotPresetGroup
import me.repeater64.advancedmpkeditor.gui.component.MinecraftSlotDisplay
import me.repeater64.advancedmpkeditor.gui.component.SmallIconAndTooltip
import me.repeater64.advancedmpkeditor.gui.screens.barrel.MinecraftItemChooserPopup
import me.repeater64.advancedmpkeditor.gui.screens.barrel.WeightedOptionListPopup

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RandomSlotPopup(
    data: RandomSlotOptionsSet,
    allLabels: MutableSet<String>,
    closePopupInputCallback: () -> Unit,
    deleteCallback: () -> Unit
) {
    WeightedOptionListPopup(
        data.options.options as SnapshotStateList<WeightedOptionEitherType<SnapshotStateList<MinecraftItem>>>, allLabels, closePopupInputCallback,

        width = 650,
        col1Weight = 0.3f,
        col2Weight = 0.15f,
        col3Weight = 0.3f,
        topContent = {
            OutlinedTextField(
                value = data.setName,
                onValueChange = { newValue: String ->
                    data.setName = newValue
                },
                modifier = Modifier.padding(bottom = 10.dp).fillMaxWidth(),
                label = { Text("Name") },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                ),
                singleLine = true
            )
        },
        firstColumnHeading = "Items",
        typeOfThing = "item set",
        toAddWhenOnlyOptionRemoved = {
            WeightedOption(listOf(DontReplaceMinecraftItem(true)).toMutableStateList())
        },
        addNewRowClicked = {
            val options = data.options.options
            options.add(WeightedOption(listOf(DontReplaceMinecraftItem(true)).toMutableStateList()))
        },
        showAddPresetButton = {
            val options = data.options.options
            options.size == 1 && (options[0].option.size == 1 && options[0].option[0] is DontReplaceMinecraftItem)
        },
        presetDropdownContents = {
            val options = data.options.options
            for (presetGroup in RandomSlotPresetGroup.entries) {
                var dropdownOpen by remember { mutableStateOf(false) }
                DropdownMenuItem(
                    text = { Text(presetGroup.displayName) },
                    leadingIcon = {
                        MinecraftSlotDisplay(
                            presetGroup.iconItem,
                            size = 50
                        ).ContentsOnly()
                    },
                    onClick = {
                        dropdownOpen = true
                    },
                    trailingIcon = {
                        DropdownMenu(dropdownOpen, onDismissRequest = {dropdownOpen = false}) {
                            for (element in presetGroup.elements) {
                                DropdownMenuItem(
                                    text = { Text(element.displayName) },
                                    onClick = {
                                        options.clear()
                                        options.addAll(element.optionsGetter().options)
                                        data.setName = presetGroup.displayName
                                    }
                                )
                            }
                        }
                    }
                )
            }
            for (preset in RandomSlotPreset.entries) {
                DropdownMenuItem(
                    text = { Text(preset.displayName) },
                    leadingIcon = {
                        MinecraftSlotDisplay(
                            preset.iconItem,
                            size = 50
                        ).ContentsOnly()
                    },
                    onClick = {
                        options.clear()
                        options.addAll(preset.optionsGetter().options)
                        data.setName = preset.displayName
                    }
                )
            }
        },
        footerLeftSideContent = {
            Button(
                onClick = {
                    closePopupInputCallback()
                    deleteCallback()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Delete")
            }
        },
        leftColumnContent = { weightedOption ->
            val list = weightedOption.option
            FlowRow(itemVerticalAlignment = Alignment.CenterVertically) {
                for ((index, item) in list.withIndex()) {
                    var showPopup by remember { mutableStateOf(false) }

                    val interactionSource = remember { MutableInteractionSource() }
                    MinecraftSlotDisplay(
                        item,
                        40,
                        tooltipContents = { Text("Left click to change item/amount\nRight click to remove item") },
                        modifier = Modifier
                            .onClick(matcher = PointerMatcher.mouse(PointerButton.Primary), onClick = {
                                showPopup = true
                            })
                            .onClick(matcher = PointerMatcher.mouse(PointerButton.Secondary), onClick = {
                                // On right click - delete
                                list.removeAt(index)
                                if (list.isEmpty()) {
                                    // Delete whole row
                                    data.options.options.remove(weightedOption)
                                }
                            })
                            .hoverable(interactionSource)
                            .indication(interactionSource, ripple())
                    ).SlotDisplay()

                    // Popup to edit the item
                    if (showPopup) {

                        MinecraftItemChooserPopup(
                            onDismiss = { showPopup = false },
                            onItemChosen = { chosenItem -> list[index] = chosenItem },
                            allowMoreThanAStack = true,
                            initiallySelectedItem = item,
                            itemToAlwaysPutAtStart = { DontReplaceMinecraftItem(true) }
                        )
                    }
                }

                var showPopup by remember { mutableStateOf(false) }
                SmallIconAndTooltip(
                    onClick = {
                        showPopup = true
                        list.add(DontReplaceMinecraftItem(true))
                    },
                    tooltipText = "Click to Add Item",
                    icon = Icons.Default.Add
                )

                if (showPopup) {
                    MinecraftItemChooserPopup(
                        onDismiss = { showPopup = false },
                        onItemChosen = { chosenItem -> list[list.lastIndex] = chosenItem },
                        allowMoreThanAStack = true,
                        initiallySelectedItem = DontReplaceMinecraftItem(true),
                        itemToAlwaysPutAtStart = { DontReplaceMinecraftItem(true) }
                    )
                }
            }
        }
    )
}