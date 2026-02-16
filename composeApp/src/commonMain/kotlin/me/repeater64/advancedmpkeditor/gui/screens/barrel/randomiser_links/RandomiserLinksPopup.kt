package me.repeater64.advancedmpkeditor.gui.screens.barrel.randomiser_links

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.RandomiserCondition
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList

enum class RandomiserLinkType(val displayName: String, val tooltipText: String) {
    ONLY_IF("Only If", "This option will only be added to the pool of possible options if an option elsewhere, which triggers the specified condition, gets chosen."),
    UNLESS("Unless", "This option will be removed from the pool of possible options if an option elsewhere, which triggers the specified condition, gets chosen."),
    TRIGGERS_EXISTING("Triggers (existing condition)", "If this option gets randomly chosen, the specified condition will be \"triggered\", which will affect options elsewhere that are set up to depend on it."),
    TRIGGERS_NEW("Triggers (create new condition)", "If this option gets randomly chosen, the specified condition will be \"triggered\", which will affect options elsewhere that are set up to depend on it.");

    fun isTrigger() = this == TRIGGERS_NEW || this == TRIGGERS_EXISTING
}

@OptIn(ExperimentalMaterial3Api::class) // ExposedDropdownMenuBox is experimental in some versions
@Composable
fun <T> RandomiserLinksPopup(
    onDismiss: () -> Unit,
    wholeList: SnapshotStateList<WeightedOption<T>>,
    option: WeightedOption<T>,
    allLabels: MutableSet<String>
) {
    val canAddTrigger = option.conditions.isEmpty()

    var typeExpanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(if (canAddTrigger) RandomiserLinkType.TRIGGERS_NEW else RandomiserLinkType.ONLY_IF) }

    var labelExpanded by remember { mutableStateOf(false) }
    var selectedLabel by remember { mutableStateOf<String?>(null) }

    var customInputLabel by remember { mutableStateOf("") }

    val isCustomInput = selectedType == RandomiserLinkType.TRIGGERS_NEW

    val actualChosenLabel = if (isCustomInput) customInputLabel else selectedLabel

    val isBlankInput = if (isCustomInput) customInputLabel.isEmpty() else selectedLabel == null
    val blankInputErrorMessage = if (isCustomInput) "Label name can't be blank!" else "Please select a condition!"

    fun checkForBadLabelAdd() : Boolean {
        // Check if this list uses any conditions depending on this label
        if (actualChosenLabel == null) return false // This'll get detected by other code as an issue
        return wholeList.any { it.conditions.any { it.conditionLabel == actualChosenLabel } }
    }

    Popup(
        alignment = Alignment.TopCenter,
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true)
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 4.dp,
            tonalElevation = 4.dp,
            modifier = Modifier.width(600.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Text(
                    text = "Add Randomiser Link",
                    style = MaterialTheme.typography.titleLarge
                )


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Type dropdown
                    ExposedDropdownMenuBox(
                        expanded = typeExpanded,
                        onExpandedChange = { typeExpanded = !typeExpanded },
                        modifier = Modifier.weight(1.3f)
                    ) {
                        OutlinedTextField(
                            value = selectedType.displayName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Type") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = typeExpanded,
                            onDismissRequest = { typeExpanded = false }
                        ) {
                            RandomiserLinkType.entries.forEach { type ->
                                if (canAddTrigger || !type.isTrigger()) {
                                    DropdownItemWithTooltip(type.displayName, type.tooltipText,
                                        onClick = {
                                            selectedType = type
                                            typeExpanded = false
                                        }
                                    )
                                } else {
                                    // Not allowed to add a trigger, cross out these options
                                    DropdownItemWithTooltip(
                                        type.displayName,
                                        "A single option can't have both triggers and conditions! To set this option as a trigger, remove all conditions.",
                                        strikethrough = true,
                                        onClick = {},
                                    )
                                }
                            }
                        }
                    }

                    // Either dropdown for existing label or text field for new label
                    if (isCustomInput) {
                        // Text field
                        OutlinedTextField(
                            value = customInputLabel,
                            onValueChange = {
                                if (it.length < 16) {
                                    customInputLabel = it
                                }
                            },
                            label = { Text("Condition Label") },
                            isError = isBlankInput,
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        // Label dropdown
                        val labelOptions = if (selectedType == RandomiserLinkType.TRIGGERS_EXISTING) {
                            allLabels.toList()
                        } else {
                            allLabels.filter { label ->
                                !wholeList.any { it.label == label } && !option.conditions.any { it.conditionLabel == label } // Filter out conditions that are triggered by a label elsewhere in this option list, and also filter out conditions that have already been added to this specific option
                            }.toList()
                        }

                        ExposedDropdownMenuBox(
                            expanded = labelExpanded,
                            onExpandedChange = { labelExpanded = !labelExpanded },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedLabel ?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Condition Label") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = labelExpanded) },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = labelExpanded,
                                onDismissRequest = { labelExpanded = false }
                            ) {
                                labelOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            selectedLabel = option
                                            labelExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Error message, if needed
                if (isBlankInput || (selectedType.isTrigger() && checkForBadLabelAdd())) {
                    Text(
                        text = if (isBlankInput) blankInputErrorMessage else "Warning - Adding this trigger will delete some conditions from this list! (Can't have trigger and conditions for the same thing in the same option list)",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                // Add button
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Button(
                        onClick = {
                            if (isBlankInput) return@Button

                            if (selectedType.isTrigger()) {
                                option.label = actualChosenLabel
                                if (checkForBadLabelAdd()) {
                                    // Need to remove all conditions from this list that look at this label
                                    wholeList.forEach { option -> option.conditions.removeAll { it.conditionLabel == actualChosenLabel } }
                                }
                                allLabels.add(actualChosenLabel!!)
                            } else {
                                option.conditions.add(RandomiserCondition(actualChosenLabel!!, selectedType == RandomiserLinkType.UNLESS))
                            }

                            onDismiss() // Close GUI
                        }
                    ) {
                        Text("Add ${if (selectedType.isTrigger()) "Trigger" else "Condition"}")
                    }
                }
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun OptionWithTooltip(
//    text: String,
//    tooltipText: String,
//    strikethrough: Boolean = false
//) {
//    val tooltipState = rememberTooltipState()
//
//    TooltipBox(
//        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
//        tooltip = {
//            PlainTooltip { Text(text = tooltipText) }
//        },
//        state = tooltipState
//    ) {
//        Text(
//            text = text,
//            textDecoration = if (strikethrough) TextDecoration.LineThrough else null,
//            modifier = Modifier.fillMaxWidth()
//        )
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownItemWithTooltip(
    text: String,
    tooltipText: String,
    strikethrough: Boolean = false,
    onClick: () -> Unit
) {
    val tooltipState = rememberTooltipState()

    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
        tooltip = {
            PlainTooltip { Text(tooltipText) }
        },
        state = tooltipState
    ) {
        DropdownMenuItem(
            text = { Text(text = text, textDecoration = if (strikethrough) TextDecoration.LineThrough else null,) },
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}