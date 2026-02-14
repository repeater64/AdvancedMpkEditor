package me.repeater64.advancedmpkeditor.gui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.onClick
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.repeater64.advancedmpkeditor.backend.data_object.item.SimpleMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.AirItem
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.BarrelItem
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.CommandBlockItem
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbarItem
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbars
import me.repeater64.advancedmpkeditor.backend.presets_examples.BlankBarrel
import me.repeater64.advancedmpkeditor.gui.component.DragDropContainer
import me.repeater64.advancedmpkeditor.gui.component.DragSwappable
import me.repeater64.advancedmpkeditor.gui.component.MinecraftItemIcon
import me.repeater64.advancedmpkeditor.gui.component.MinecraftSlotDisplay
import me.repeater64.advancedmpkeditor.gui.component.SimpleDropdown
import me.repeater64.advancedmpkeditor.gui.component.verticalColumnScrollbar
import me.repeater64.advancedmpkeditor.gui.platform.HotbarNbtFileManager
import me.repeater64.advancedmpkeditor.gui.platform.PreventAppExit
import me.repeater64.advancedmpkeditor.gui.screens.barrel.BarrelEditor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class, ExperimentalFoundationApi::class) // Needed for ExposedDropdownMenuBox
@Composable
fun EditorScreen(
    hotbars: SavedHotbars,
    fileManager: HotbarNbtFileManager,

    pendingNavigationRequest: Screen?,
    onConfirmNavigation: (Screen) -> Unit,
    onCancelNavigation: () -> Unit
) {

    var savedHotbarsHash by remember { mutableStateOf(hotbars.contentHash()) }
    val currentHotbarsHash by produceState(initialValue = savedHotbarsHash, key1 = hotbars) {
        snapshotFlow { hotbars.contentHash() }
            .collect { value = it }
    }
    val isSaved = (currentHotbarsHash == savedHotbarsHash )

    var isSaving by remember { mutableStateOf(false) }
    var showUnsavedDialog by remember { mutableStateOf(false) }

    var showGeneralWarningDialog by remember { mutableStateOf(false) }
    var generalWarningDialogText by remember { mutableStateOf("nothing yet") }
    var generalWarningProceedAction by remember { mutableStateOf<() -> Any>({}) }

    var showEditorDialog by remember { mutableStateOf(false) }
    var editorDialog by remember { mutableStateOf<@Composable () -> Any>( {} ) }

    var selectedHotbarIndex by remember { mutableStateOf(0) }

    var currentlyEditingItemIndex by remember { mutableStateOf(-1) }
    fun currentlyEditingItem(): SavedHotbarItem = if (currentlyEditingItemIndex >= 0) hotbars.hotbars[selectedHotbarIndex].hotbarItems[currentlyEditingItemIndex] else AirItem()


    PreventAppExit(!isSaved)

    LaunchedEffect(pendingNavigationRequest) {
        if (pendingNavigationRequest != null) {
            if (isSaved) {
                // Safe to go, approve immediately
                onConfirmNavigation(pendingNavigationRequest)
            } else {
                // Not saved, trigger the local dialog
                showUnsavedDialog = true
            }
        }
    }

    fun showGeneralWarning(dialogText: String, proceedAction: () -> Any) {
        generalWarningDialogText = dialogText
        generalWarningProceedAction = proceedAction
        showGeneralWarningDialog = true
    }

    @Composable
    fun saveButton() {
        Button(
            onClick = {
                isSaving = true
                fileManager.saveHotbars(hotbars, "hotbar.nbt")
                isSaving = false
                savedHotbarsHash = currentHotbarsHash
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(if (isSaving) "Saving..." else "Download / Save All")
        }
    }

    @Composable
    fun topPanel() {
        // Top panel with hotbar selector and saved/unsaved text
        @Composable
        fun BoxScope.unsavedChangesText() {
            Row(modifier = Modifier.align(Alignment.CenterStart)) {
                Spacer(Modifier.width(50.dp))
                Text(
                    if (isSaved) "All Changes Saved" else "Unsaved Changes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isSaved) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(Modifier.height(5.dp))
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {

            // Unsaved changes/all changes saved text
            unsavedChangesText()

            // Hotbar selector dropdown
            key(selectedHotbarIndex) {SimpleDropdown(
                modifier = Modifier.width(300.dp).align(Alignment.Center),
                label = "Select Hotbar",
                selectedValue = "Hotbar ${selectedHotbarIndex + 1}",
                options = hotbars.hotbars.indices.map { "Hotbar ${it + 1}" },
                optionSelected = { selectedHotbarIndex = it; currentlyEditingItemIndex = -1 },
            )}

            // Save all button
            Row(modifier = Modifier.align(Alignment.CenterEnd)) { saveButton(); Spacer(Modifier.width(50.dp)) }
        }
        Spacer(Modifier.height(12.dp))
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        topPanel()

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalColumnScrollbar(scrollState).verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Hotbar items selector
            DragDropContainer(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center,
                onSwap = { srcKey, destKey ->
                    val hotbar = hotbars.hotbars[selectedHotbarIndex]
                    val srcData = hotbar.hotbarItems[srcKey]
                    hotbar.hotbarItems[srcKey] = hotbar.hotbarItems[destKey]
                    hotbar.hotbarItems[destKey] = srcData

                    if (currentlyEditingItemIndex == srcKey && srcData is BarrelItem) {
                        // We moved the currently editing barrel to destKey. Follow it
                        currentlyEditingItemIndex = destKey
                    } else if (currentlyEditingItemIndex == destKey && hotbar.hotbarItems[srcKey] is BarrelItem) {
                        // We moved something else into the currently editing slot, so the barrel we were editing has moved to srcKey. Follow it
                        currentlyEditingItemIndex = srcKey
                    }
                },
                emptyChecker = {hotbars.hotbars[selectedHotbarIndex].hotbarItems[it] is AirItem}
            ) {
                Row {
                    val selectedHotbarItems = hotbars.hotbars[selectedHotbarIndex].hotbarItems
                    for ((i, savedHotbarItem) in selectedHotbarItems.withIndex()) {
                        var dropdownExpanded by remember {mutableStateOf(false)}

                        val onLeftClick = {
                            if (savedHotbarItem is AirItem) { // Open dropdown with add barrel/add cmd block options
                                dropdownExpanded = true
                            } else if (savedHotbarItem is BarrelItem) {
                                currentlyEditingItemIndex = i
                            }
                        }
                        val onRightClick = {
                            if (savedHotbarItem !is AirItem) {
                                val deleteAction = {
                                    selectedHotbarItems[i] = AirItem()

                                    if (currentlyEditingItemIndex == i) {
                                        currentlyEditingItemIndex = -1
                                    }
                                }
                                if (savedHotbarItem is BarrelItem) {
                                    showGeneralWarning("Are you sure you want to delete the entire configuration barrel \"${savedHotbarItem.name}\"? This can't be undone!", deleteAction)
                                } else {
                                    deleteAction()
                                }
                            }
                        }

                        val slotDisplay = MinecraftSlotDisplay(savedHotbarItem.getGuiRepresentationItem(), 100,
                            tooltipContents = {
                                Column {
                                    Text(savedHotbarItem.getGuiName(), style = MaterialTheme.typography.titleSmallEmphasized, fontWeight = FontWeight.Bold)
                                    Spacer(Modifier.height(5.dp))
                                    when (savedHotbarItem) {
                                        is AirItem -> Text("Click to add item", style = MaterialTheme.typography.bodyMedium)
                                        is BarrelItem -> Text("Left Click to edit, Right Click to delete", style = MaterialTheme.typography.bodyMedium)
                                        is CommandBlockItem -> Text("Right Click to delete", style = MaterialTheme.typography.bodyMedium)
                                    }
                                    if (savedHotbarItem !is AirItem) {
                                        Spacer(Modifier.height(5.dp))
                                        Text("Click and drag to swap hotbar slots", style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            },
                            highlighted = currentlyEditingItemIndex == i,
                            modifier = Modifier.onClick(matcher = PointerMatcher.mouse(PointerButton.Primary), onClick = onLeftClick)
                                .onClick(matcher = PointerMatcher.mouse(PointerButton.Secondary), onClick = onRightClick)
                        )

                        Spacer(Modifier.width(2.dp))
                        Box(contentAlignment = Alignment.Center) {
                            DragSwappable(
                                key = i,
                                ghostContent = { slotDisplay.ContentsOnly() },
                                content = { isDragging -> slotDisplay.SlotDisplay(!isDragging) }
                            )
                            DropdownMenu(expanded = dropdownExpanded, onDismissRequest = { dropdownExpanded = false }) {
                                DropdownMenuItem(
                                    text = { Text("Add Configuration Barrel") },
                                    leadingIcon = { MinecraftItemIcon(SimpleMinecraftItem("barrel", 1), modifier=Modifier.size(50.dp)) },
                                    onClick = {
                                        // Add barrel to i slot
                                        selectedHotbarItems[i] = BlankBarrel.barrel
                                        dropdownExpanded = false
                                        currentlyEditingItemIndex = i
                                    }
                                )
                                Spacer(Modifier.height(8.dp))
                                DropdownMenuItem(
                                    text = { Text("Add MPK Command Block") },
                                    leadingIcon = { MinecraftItemIcon(SimpleMinecraftItem("repeating_command_block", 1), modifier=Modifier.size(50.dp)) },
                                    onClick = {
                                        // Add cmd block to i slot
                                        selectedHotbarItems[i] = CommandBlockItem()
                                        dropdownExpanded = false
                                    }
                                )
                            }
                        }
                        Spacer(Modifier.width(2.dp))
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Barrel editor, or text telling you to select a barrel
            key(currentlyEditingItemIndex) { Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                val currentlyEditingItem = currentlyEditingItem()
                if (currentlyEditingItem !is BarrelItem) {
                    Spacer(Modifier.height(100.dp))
                    Text(
                        text = "Click on a barrel (or add one) to edit its configuration!",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                else {
                    BarrelEditor(
                        barrelItem = currentlyEditingItem,
                        showDialogCallback = { dialog ->
                            showEditorDialog = true
                            editorDialog = dialog
                        },
                        hideDialogCallback = {showEditorDialog = false}
                    )
                }
            }}

            Spacer(Modifier.weight(1f)) // Push save button to bottom
            Spacer(Modifier.height(24.dp)) // A bit of space even if no spare room to push save button all the way to bottom


            saveButton()

            Spacer(Modifier.height(24.dp))
        }

        if (showUnsavedDialog) {
            UnsavedChangesDialog(
                onConfirmExit = {
                    showUnsavedDialog = false
                    onConfirmNavigation(pendingNavigationRequest!!)
                },
                onDismiss = {
                    showUnsavedDialog = false
                    onCancelNavigation() // Tell parent to clear the request
                }
            )
        } else if (showGeneralWarningDialog) {
            GeneralWarningDialog(
                warningText = generalWarningDialogText,
                onProceed = {
                    showGeneralWarningDialog = false
                    generalWarningProceedAction()
                },
                onDismiss = {
                    showGeneralWarningDialog = false
                }
            )
        } else if (showEditorDialog) {
            editorDialog()
        }
    }
}

@Composable
fun UnsavedChangesDialog(
    onConfirmExit: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Unsaved Changes") },
        text = { Text("You have unsaved changes. Are you sure you want to exit?") },
        confirmButton = {
            Button(
                onClick = onConfirmExit,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Exit (Discard Changes)")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun GeneralWarningDialog(
    warningText: String,
    onProceed: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Warning") },
        text = { Text(warningText) },
        confirmButton = {
            Button(
                onClick = onProceed,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Proceed")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}