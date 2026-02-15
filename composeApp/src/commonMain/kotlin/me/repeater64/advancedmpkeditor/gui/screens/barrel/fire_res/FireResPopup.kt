package me.repeater64.advancedmpkeditor.gui.screens.barrel.fire_res

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.repeater64.advancedmpkeditor.backend.data_object.fire_res.FireResSettings
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HealthHungerOption
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HealthOption
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HungerOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.gui.screens.barrel.WeightedOptionListPopup

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FireResPopup(
    fireResSettings: FireResSettings,
    allLabels: MutableSet<String>,
    closePopupInputCallback: () -> Unit
) {
    WeightedOptionListPopup(
        fireResSettings.options, allLabels, closePopupInputCallback,

        width = 550,
        col1Weight = 0.3f,
        col2Weight = 0.15f,
        col3Weight = 0.5f,

        topContent = {
            Text(
                text = "Fire Resistance Effect Durations",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        firstColumnHeading = "Seconds",
        typeOfThing = "option",
        toAddWhenOnlyOptionRemoved = { WeightedOption(0) },
        addNewRowClicked = {
            fireResSettings.options.options.add(WeightedOption(0))
        },
        showAddPresetButton = { false },
        presetDropdownContents = {},
        footerLeftSideContent = {},
        leftColumnContent = { weightedOption ->
            var isFocused by remember { mutableStateOf(false) }
            val borderColor = if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
            BasicTextField(
                value = weightedOption.option.toString(),
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || (newValue.all { it.isDigit() } && (newValue.toIntOrNull() ?: 0) > 0)) {
                        weightedOption.option = if (newValue.isEmpty()) 0 else newValue.toInt()
                    }
                },
                modifier = Modifier
                    .width(70.dp)
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
    )
}