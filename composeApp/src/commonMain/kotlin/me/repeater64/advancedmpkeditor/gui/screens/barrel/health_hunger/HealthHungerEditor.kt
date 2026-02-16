package me.repeater64.advancedmpkeditor.gui.screens.barrel.health_hunger

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HealthHungerSettings

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ColumnScope.HealthHungerEditor(
    healthHungerSettings: HealthHungerSettings,
    allLabels: MutableSet<String>,
    showDialogCallback: (@Composable () -> Any) -> Any,
    hideDialogCallback: () -> Unit
) {
    Text("Health + Hunger Settings", style = MaterialTheme.typography.headlineLarge)
    Spacer(Modifier.height(25.dp))
    OutlinedButton(
        onClick = {
            showDialogCallback {
                HealthHungerPopup(healthHungerSettings, allLabels, hideDialogCallback)
            }
        },
//        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    ) {
        Text("Click to Edit", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
    }
}