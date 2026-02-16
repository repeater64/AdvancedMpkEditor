package me.repeater64.advancedmpkeditor.gui.screens.barrel.fire_res

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
import me.repeater64.advancedmpkeditor.backend.data_object.fire_res.FireResSettings

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ColumnScope.FireResEditor(
    fireResSettings: FireResSettings,
    allLabels: MutableSet<String>,
    showDialogCallback: (@Composable () -> Any) -> Any,
    hideDialogCallback: () -> Unit
) {
    Text("Fire Resistance Effect Settings", style = MaterialTheme.typography.headlineLarge)
    Spacer(Modifier.height(25.dp))
    OutlinedButton(
        onClick = {
            showDialogCallback {
                FireResPopup(fireResSettings, allLabels, hideDialogCallback)
            }
        },
//        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary)
    ) {
        Text("Click to Edit", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
    }
}