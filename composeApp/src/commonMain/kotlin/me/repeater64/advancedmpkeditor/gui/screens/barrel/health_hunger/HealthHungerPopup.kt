package me.repeater64.advancedmpkeditor.gui.screens.barrel.health_hunger

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HealthHungerOption
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HealthHungerSettings
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HealthOption
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HungerOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionEitherType
import me.repeater64.advancedmpkeditor.gui.screens.barrel.WeightedOptionListPopup

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HealthHungerPopup(
    healthHungerSettings: HealthHungerSettings,
    allLabels: MutableSet<String>,
    closePopupInputCallback: () -> Unit
) {
    WeightedOptionListPopup(
        healthHungerSettings.options as SnapshotStateList<WeightedOptionEitherType<HealthHungerOption>>, allLabels, closePopupInputCallback,

        width = 800,
        col1Weight = 0.45f,
        col2Weight = 0.08f,
        col3Weight = 0.33f,

        topContent = {
            Text(
                text = "Health + Hunger Settings",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        firstColumnHeading = "Option",
        typeOfThing = "option",
        toAddWhenOnlyOptionRemoved = { WeightedOption(HealthHungerOption(HealthOption.FULL_HEALTH, HungerOption.HUNGER_RESET)) },
        addNewRowClicked = {
            healthHungerSettings.options.options.add(WeightedOption(HealthHungerOption(HealthOption.FULL_HEALTH, HungerOption.HUNGER_RESET)))
        },
        showAddPresetButton = { false },
        presetDropdownContents = {},
        footerLeftSideContent = {},
        leftColumnContent = { weightedOption ->
            Row {
            Spacer(Modifier.width(10.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(0.75f),
                horizontalArrangement = Arrangement.Start,
                itemVerticalAlignment = Alignment.CenterVertically
            ) {
                // Health Option Dropdown
                var healthExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = healthExpanded,
                    onExpandedChange = { healthExpanded = !healthExpanded },
                ) {
                    OutlinedTextField(
                        value = weightedOption.option.healthOption.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Heath Option") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = healthExpanded) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = healthExpanded,
                        onDismissRequest = { healthExpanded = false }
                    ) {
                        HealthOption.entries.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(text = option.displayName) },
                                onClick = {
                                    weightedOption.option.healthOption = option
                                    healthExpanded = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // Hunger Option Dropdown
                var hungerExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = hungerExpanded,
                    onExpandedChange = { hungerExpanded = !hungerExpanded },
                ) {
                    OutlinedTextField(
                        value = weightedOption.option.hungerOption.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Hunger Option") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = hungerExpanded) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = hungerExpanded,
                        onDismissRequest = { hungerExpanded = false }
                    ) {
                        HungerOption.entries.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(text = option.displayName) },
                                onClick = {
                                    weightedOption.option.hungerOption = option
                                    hungerExpanded = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
            }
        }
    )
}