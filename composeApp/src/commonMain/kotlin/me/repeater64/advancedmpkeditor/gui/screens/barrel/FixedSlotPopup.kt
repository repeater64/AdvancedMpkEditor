package me.repeater64.advancedmpkeditor.gui.screens.barrel

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.FixedSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.InventorySlotData
import me.repeater64.advancedmpkeditor.backend.data_object.item.DontReplaceMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.ForcedEmptyMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItemCategory
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.backend.presets_examples.availableItem
import me.repeater64.advancedmpkeditor.backend.presets_examples.emptyItem
import me.repeater64.advancedmpkeditor.gui.component.DeleteIconAndTooltip
import me.repeater64.advancedmpkeditor.gui.component.MinecraftSlotDisplay
import me.repeater64.advancedmpkeditor.gui.component.SmallIconAndTooltip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FixedSlotPopup(
    fixedSlotData: FixedSlotData,
    allLabels: MutableSet<String>,
    closePopupInputCallback: () -> Unit,
    onlyOneItemCategory: MinecraftItemCategory? = null
) {
    val col1Weight = 0.2f
    val col2Weight = 0.13f
    val col3Weight = 0.3f

    fun closePopup() {
        // Fix any weights that are "zero" (due to being left empty) to 1
        fixedSlotData.itemOptions.options.forEach { if (it.weight == 0) it.weight = 1 }
        closePopupInputCallback()
    }

    Dialog(onDismissRequest = { closePopup() }) {
        Surface(
            modifier = Modifier
                .width(500.dp)
                .height(600.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Items for ${fixedSlotData.slotDisplayName}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                // Table Headings
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(col1Weight), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Item",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }

                    Box(modifier = Modifier.weight(col2Weight), contentAlignment = Alignment.Center) {
                        HeaderWithTooltip(
                            text = "Weight",
                            tooltipText = "Controls the relative chance of this item being chosen to be in this slot. For example, if all weights are 1, all are equally likely. If one is 2, that one is twice as likely."
                        )
                    }

                    Box(modifier = Modifier.weight(col3Weight), contentAlignment = Alignment.Center) {
                        HeaderWithTooltip(
                            text = "Randomiser Links",
                            tooltipText = "Allows you to link which item is chosen here to other randomly chosen options. For any item, you can either set it to \"trigger\" a condition, or to depend on a condition (or set of conditions). If you set an item to depend on conditions, it will only be added to the pool of available options if those conditions are met. If you set it to depend on multiple conditions, they must all be met (ie they are treated as AND). For more explanation about this, see the instructions page (link in top right).",
                            tooltipModifier = Modifier.width(350.dp)
                        )
                    }
                }

                // Scrollable rows
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth()
                ) {
                    items(fixedSlotData.itemOptions.options.size) { index ->
                        val options = fixedSlotData.itemOptions.options
                        val option = options[index]
                        ItemRow(
                            option = option,
                            wholeList = fixedSlotData.itemOptions,
                            allLabels = allLabels,
                            isOnlyRow = options.size == 1,
                            col1Weight = col1Weight,
                            col2Weight = col2Weight,
                            col3Weight = col3Weight,
                            onlyOneItemCategory = onlyOneItemCategory,
                            onDelete = {
                                options.remove(option)
                                if (options.isEmpty()) {
                                    if (fixedSlotData is InventorySlotData) {
                                        // Default to "available for random items"
                                        options.add(availableItem())
                                    } else {
                                        // Default to empty item
                                        options.add(emptyItem())
                                    }
                                }
                            }
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    }

                    // Add new button
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clickable {
                                    val options = fixedSlotData.itemOptions.options

                                    // Check if this is an available for random items slot, in which case delete that item before adding new one
                                    if (options.size == 1 && options[0].option is DontReplaceMinecraftItem) {
                                        options.clear()
                                    }

                                    // Add new row
                                    options.add(WeightedOption(ForcedEmptyMinecraftItem()))
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Row",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // Footer with ok button on right, and other button on left if it's an inventory slot
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = { closePopup() }
                    ) {
                        Text("Ok")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ItemRow(
    option: WeightedOption<MinecraftItem>,
    wholeList: WeightedOptionList<MinecraftItem>,
    allLabels: MutableSet<String>,
    isOnlyRow: Boolean,
    col1Weight: Float,
    col2Weight: Float,
    col3Weight: Float,
    onDelete: () -> Unit,
    onlyOneItemCategory: MinecraftItemCategory?,
) {

    // Check if this is a "available for random items" slot
    if (isOnlyRow && option.option is DontReplaceMinecraftItem) {
        // Don't render normal row, just info saying this slot is available for random items
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier= Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 8.dp)) {
            Spacer(Modifier.height(15.dp))
            Text("Slot is Available for Random Items", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
            Spacer(Modifier.height(15.dp))
        }
        return
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Delete icon
        DeleteIconAndTooltip(onDelete)

        // Item column
        Box(modifier = Modifier.weight(col1Weight), contentAlignment = Alignment.Center) {
            var showPopup by remember { mutableStateOf(false) }

            MinecraftSlotDisplay(
                option.option,
                50,
                tooltipContents = {Text("Click to change item/amount")},
                modifier = Modifier.onClick(onClick = { showPopup = true })
            ).SlotDisplay()

            // Popup to edit the item
            if (showPopup) {

                MinecraftItemChooserPopup(
                    onDismiss = { showPopup = false },
                    onItemChosen = {chosenItem -> option.option = chosenItem },
                    allowMoreThanAStack = false,
                    initiallySelectedItem = option.option,
                    onlyOneCategory = onlyOneItemCategory
                )
            }
        }

        // Weight column
        Box(modifier = Modifier.weight(col2Weight), contentAlignment = Alignment.Center) {
            var isFocused by remember { mutableStateOf(false) }
            val borderColor = if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
            BasicTextField(
                value = if (option.weight == 0 ) "" else option.weight.toString(), // Need to allow zero as an option so they can, for example, delete "1" temporarily to allow "2"
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || (newValue.all { it.isDigit() } && (newValue.toIntOrNull() ?: 0) > 0)) {
                        option.weight = if (newValue.isEmpty()) 0 else newValue.toInt()
                    }
                },
                modifier = Modifier
                    .width(40.dp)
                    .height(34.dp)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    }
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(4.dp))
                    .border(
                        width = if (isFocused) 2.dp else 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 6.dp),

                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                cursorBrush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.primary),
                // For centering
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        innerTextField()
                    }
                }
            )
        }

        // Randomiser Links column
        Box(modifier = Modifier.weight(col3Weight), contentAlignment = Alignment.Center) {
            var showPopup by remember { mutableStateOf(false) }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (option.label != null) {
                    // It's a trigger
                    RandomiserLinkRemovableChip(option.label!!, onRemove = { option.label = null })
                } else {
                    val onlyOne = option.conditions.size == 1
                    for (condition in option.conditions) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RandomiserLinkRemovableChip(condition, onRemove = { option.conditions.remove(condition) })
                            if (onlyOne) {
                                Spacer(Modifier.width(10.dp))
                                SmallIconAndTooltip(
                                    onClick = { showPopup = true },
                                    tooltipText = "Click to Add Condition",
                                    icon = Icons.Default.Add
                                )
                            }
                        }
                    }
                    if (!onlyOne) {
                        SmallIconAndTooltip(
                            onClick = { showPopup = true },
                            tooltipText = "Click to Add Condition",
                            icon = Icons.Default.Add
                        )
                    }
                }
            }

            // Popup to edit the randomiser links
            if (showPopup) {
                RandomiserLinksPopup(
                    onDismiss = { showPopup = false },
                    wholeList = wholeList,
                    option = option,
                    allLabels = allLabels
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderWithTooltip(
    text: String,
    tooltipText: String,
    modifier: Modifier = Modifier,
    tooltipModifier: Modifier = Modifier
) {
    val tooltipState = rememberTooltipState()

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
            tooltip = {
                PlainTooltip(modifier = tooltipModifier) { Text(text = tooltipText) }
            },
            state = tooltipState,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Hover for Info",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}