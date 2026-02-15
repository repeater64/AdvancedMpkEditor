package me.repeater64.advancedmpkeditor.gui.screens.barrel

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import me.repeater64.advancedmpkeditor.backend.data_object.item.DontReplaceMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.gui.component.DeleteIconAndTooltip
import me.repeater64.advancedmpkeditor.gui.component.SmallIconAndTooltip
import me.repeater64.advancedmpkeditor.gui.screens.barrel.randomiser_links.RandomiserLinkRemovableChip
import me.repeater64.advancedmpkeditor.gui.screens.barrel.randomiser_links.RandomiserLinksPopup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> WeightedOptionListPopup(
    data: WeightedOptionList<T>,
    allLabels: MutableSet<String>,
    closePopupInputCallback: () -> Unit,

    topContent: @Composable (ColumnScope.() -> Unit),
    firstColumnHeading: String,
    typeOfThing: String,
    toAddWhenOnlyOptionRemoved: () -> WeightedOption<T>,
    addNewRowClicked: () -> Unit,
    showAddPresetButton: () -> Boolean,
    presetDropdownContents: @Composable ColumnScope.() -> Unit,
    footerLeftSideContent: @Composable RowScope.() -> Unit,
    leftColumnContent: @Composable BoxScope.(WeightedOption<T>) -> Unit,

    col1Weight: Float = 0.2f,
    col2Weight: Float = 0.13f,
    col3Weight: Float = 0.3f,
    width: Int = 500,
) {

    fun closePopup() {
        // Fix any weights that are "zero" (due to being left empty) to 1
        data.options.forEach { if (it.weight == 0) it.weight = 1 }
        closePopupInputCallback()
    }

    Dialog(onDismissRequest = { closePopup() }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(
            modifier = Modifier
                .width(width.dp)
                .height(600.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                topContent()

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
                            text = firstColumnHeading,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }

                    Box(modifier = Modifier.weight(col2Weight), contentAlignment = Alignment.Center) {
                        HeaderWithTooltip(
                            text = "Weight",
                            tooltipText = "Controls the relative chance of this $typeOfThing being chosen. For example, if all weights are 1, all are equally likely. If one is 2, that one is twice as likely."
                        )
                    }

                    Box(modifier = Modifier.weight(col3Weight), contentAlignment = Alignment.Center) {
                        HeaderWithTooltip(
                            text = "Randomiser Links",
                            tooltipText = "Allows you to link which $typeOfThing is chosen here to other randomly chosen options. For any $typeOfThing, you can either set it to \"trigger\" a condition, or to depend on a condition (or set of conditions). If you set an $typeOfThing to depend on conditions, it will only be added to the pool of available options if those conditions are met. If you set it to depend on multiple conditions, they must all be met (ie they are treated as AND). For more explanation about this, see the instructions page (link in top right).",
                            tooltipModifier = Modifier.width(350.dp)
                        )
                    }
                }

                // Scrollable rows
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth()
                ) {
                    val options = data.options
                    items(options.size) { index ->
                        val option = options[index]
                        WeightedOptionRow(
                            option = option,
                            leftColumnContent = leftColumnContent,
                            wholeList = data,
                            allLabels = allLabels,
                            isOnlyRow = options.size == 1,
                            col1Weight = col1Weight,
                            col2Weight = col2Weight,
                            col3Weight = col3Weight,
                            onDelete = {
                                options.remove(option)
                                if (options.isEmpty()) {
                                    options.add(toAddWhenOnlyOptionRemoved())
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
                                    addNewRowClicked()
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

                    // Use preset button, if we should
                    if (showAddPresetButton()) {
                        item {
                            var dropdownExpanded by remember { mutableStateOf(false) }

                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .clickable {
                                        dropdownExpanded = true
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Use a Preset", style = MaterialTheme.typography.titleSmall, textAlign = TextAlign.Center)

                                DropdownMenu(expanded = dropdownExpanded, onDismissRequest = { dropdownExpanded = false }) {
                                    presetDropdownContents()
                                }
                            }
                        }

                    }
                }

                // Footer with ok button on right, and potential other content on left (used for fixed inventory slots)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    footerLeftSideContent()

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
fun <T> WeightedOptionRow(
    option: WeightedOption<T>,
    leftColumnContent: @Composable BoxScope.(WeightedOption<T>) -> Unit,
    wholeList: WeightedOptionList<T>,
    allLabels: MutableSet<String>,
    isOnlyRow: Boolean,
    col1Weight: Float,
    col2Weight: Float,
    col3Weight: Float,
    onDelete: () -> Unit
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
            leftColumnContent(option)
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
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
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