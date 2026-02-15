package me.repeater64.advancedmpkeditor.gui.screens.barrel.fixed_slot

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.FixedSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.InventorySlotData
import me.repeater64.advancedmpkeditor.backend.data_object.item.DontReplaceMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.ForcedEmptyMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItemCategory
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.backend.presets_examples.FixedSlotPreset
import me.repeater64.advancedmpkeditor.backend.presets_examples.availableItem
import me.repeater64.advancedmpkeditor.backend.presets_examples.emptyItem
import me.repeater64.advancedmpkeditor.gui.component.MinecraftSlotDisplay
import me.repeater64.advancedmpkeditor.gui.component.MinecraftSlotDisplayMulti
import me.repeater64.advancedmpkeditor.gui.screens.barrel.MinecraftItemChooserPopup
import me.repeater64.advancedmpkeditor.gui.screens.barrel.WeightedOptionListPopup

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FixedSlotPopup(
    fixedSlotData: FixedSlotData,
    allLabels: MutableSet<String>,
    closePopupInputCallback: () -> Unit,
    onlyOneItemCategory: MinecraftItemCategory? = null
) {
    WeightedOptionListPopup(
        fixedSlotData.itemOptions, allLabels, closePopupInputCallback,

        topContent = {
            Text(
                text = "Items for ${fixedSlotData.slotDisplayName}",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        firstColumnHeading = "Item",
        typeOfThing = "item",
        toAddWhenOnlyOptionRemoved = { if (fixedSlotData is InventorySlotData) availableItem() else emptyItem() },
        addNewRowClicked = {
            // Check if this is an available for random items slot, in which case delete that item before adding new one
            val options = fixedSlotData.itemOptions.options
            if (options.size == 1 && options[0].option is DontReplaceMinecraftItem) {
                options.clear()
            }

            // Add new row
            options.add(WeightedOption(ForcedEmptyMinecraftItem()))
        },
        showAddPresetButton = {
            val options = fixedSlotData.itemOptions.options
            options.size == 1 && (options[0].option is DontReplaceMinecraftItem || options[0].option is ForcedEmptyMinecraftItem)
        },
        presetDropdownContents = {
            val options = fixedSlotData.itemOptions.options
            for (preset in FixedSlotPreset.entries.filter { it.onlyIfOneCategory == onlyOneItemCategory }) {
                DropdownMenuItem(
                    text = { Text(preset.displayName) },
                    leadingIcon = {
                        MinecraftSlotDisplayMulti(
                            preset.options,
                            size = 50
                        ).ContentsOnly()
                    },
                    onClick = {
                        options.clear()
                        options.addAll(preset.optionsGetter().options)
                    }
                )
            }
        },
        footerLeftSideContent = {
            if (fixedSlotData is InventorySlotData) {
                if (fixedSlotData.itemOptions.options.size == 1 && fixedSlotData.itemOptions.options[0].option is DontReplaceMinecraftItem) {
                    // This is available for random items, button to switch to forced empty
                    Button(
                        onClick = {
                            fixedSlotData.itemOptions.options.clear()
                            fixedSlotData.itemOptions.options.add(emptyItem())
                        }
                    ) {
                        Text("Switch to Forced Empty Slot")
                    }
                } else {
                    // Button to switch to available for random items
                    Button(
                        onClick = {
                            fixedSlotData.itemOptions.options.clear()
                            fixedSlotData.itemOptions.options.add(availableItem())
                        }
                    ) {
                        Text("Make Slot Available for Random Items${if (fixedSlotData.itemOptions.options.size == 1 && fixedSlotData.itemOptions.options[0].option is ForcedEmptyMinecraftItem) "" else " (Clears All)"}")
                    }
                }
            }
        },
        leftColumnContent = { option ->
            var showPopup by remember { mutableStateOf(false) }
            val interactionSource = remember { MutableInteractionSource() }

            MinecraftSlotDisplay(
                option.option,
                50,
                tooltipContents = { Text("Click to change item/amount") },
                modifier = Modifier.onClick(onClick = { showPopup = true })
                    .hoverable(interactionSource)
                    .indication(interactionSource, ripple())
            ).SlotDisplay()

            // Popup to edit the item
            if (showPopup) {

                MinecraftItemChooserPopup(
                    onDismiss = { showPopup = false },
                    onItemChosen = { chosenItem -> option.option = chosenItem },
                    allowMoreThanAStack = false,
                    initiallySelectedItem = option.option,
                    onlyOneCategory = onlyOneItemCategory
                )
            }
        }
    )
}