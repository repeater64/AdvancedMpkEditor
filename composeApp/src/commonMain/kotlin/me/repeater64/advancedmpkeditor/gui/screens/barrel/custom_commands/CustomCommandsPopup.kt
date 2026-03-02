package me.repeater64.advancedmpkeditor.gui.screens.barrel.custom_commands

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.gui.component.DeleteIconAndTooltip
import me.repeater64.advancedmpkeditor.gui.component.SmallIconAndTooltip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomCommandsPopup(
    commands: SnapshotStateList<String>,
    closePopupCallback: () -> Unit,

    title: String,
    info: String,
) {

    fun onClose() {
        // Remove any blank commands, remove / at start of any commands
        commands.removeAll { it.isBlank() }
        for ((index, command) in commands.withIndex()) {
            if (command.startsWith("/")) {
                commands[index] = command.removePrefix("/")
            }
        }

        closePopupCallback()
    }

    Dialog(onDismissRequest = { onClose() }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(
            modifier = Modifier
                .width(600.dp)
                .height(600.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Title + Info text
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Text(
                    text=info,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                )

                // Scrollable rows
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth()
                ) {
                    items(commands.size) { index ->
                        CommandRow(
                            commands = commands,
                            index = index,
                            onDelete = {
                                commands.removeAt(index)
                            },
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
                                    commands.add("")
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Command",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // Footer with ok button on right
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = { onClose() }
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
fun CommandRow(
    commands: SnapshotStateList<String>,
    index: Int,
    onDelete: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Delete icon
        DeleteIconAndTooltip(onDelete)

        // Duplicate
        SmallIconAndTooltip(
            onClick = {
                commands.add(index, commands[index])
            },
            tooltipText = "Duplicate",
            icon = Icons.Default.ContentCopy
        )

        Spacer(Modifier.width(20.dp))

        var isFocused by remember { mutableStateOf(false) }
        val borderColor = if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
        BasicTextField(
            value = commands[index],
            onValueChange = { newValue ->
                commands[index] = newValue
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
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
}