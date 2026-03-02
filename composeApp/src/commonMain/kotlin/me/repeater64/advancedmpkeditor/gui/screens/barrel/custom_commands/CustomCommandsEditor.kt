package me.repeater64.advancedmpkeditor.gui.screens.barrel.custom_commands

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.repeater64.advancedmpkeditor.backend.commands.CustomCommandSettings

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ColumnScope.CustomCommandsEditor(
    customCommandSettings: CustomCommandSettings,
    showDialogCallback: (@Composable () -> Any) -> Any,
    hideDialogCallback: () -> Unit
) {
    Text("Custom Commands", style = MaterialTheme.typography.headlineLarge)
    Spacer(Modifier.height(25.dp))

    OutlinedButton(
        onClick = {
            showDialogCallback {
                showDialogCallback { CustomCommandsPopup(customCommandSettings.preItemsCommands,hideDialogCallback, "Pre-Items Custom Commands", "Custom commands that will be run as soon as you activate MPK with this barrel, before any items are given to you. Note that if you put any items in the inventory at this stage it will mess with receiving normal items.") }
            }
        },
        border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
    ) {
        Text("Pre-Items Custom Commands", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
    }

    Spacer(Modifier.height(15.dp))

    OutlinedButton(
        onClick = {
            showDialogCallback {
                showDialogCallback { CustomCommandsPopup(customCommandSettings.postItemsCommands,hideDialogCallback, "Post-Items Custom Commands", "Custom commands that will be run after all items have been given to the player, but just before they are actually teleported into the split.") }
            }
        },
        border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
    ) {
        Text("Post-Items, Pre-Teleport Custom Commands", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
    }

    Spacer(Modifier.height(15.dp))

    OutlinedButton(
        onClick = {
            showDialogCallback {
                showDialogCallback { CustomCommandsPopup(customCommandSettings.postTeleportCommands,hideDialogCallback, "Post-Teleport Custom Commands", "Custom commands that will be run just after you've been teleported into the split. This is equivalent to commands in \"AUTO\" books in standard MPK. Add the command \"data remove storage pk I\" here to fix the MPK bug where the dragon loads a tiny bit earlier than usual, making hard zeroes a little harder (note that this will break potion scripts like force perch and reblind on surface).") }
            }
        },
        border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
    ) {
        Text("Post-Teleport Custom Commands", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
    }
}