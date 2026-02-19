package me.repeater64.advancedmpkeditor.gui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InstructionsScreen() {

    BasicFullyScrollableScreen(Modifier.fillMaxWidth()) {
        // TODO add instructions info
        Text(
            text = "Instructions",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = """
                1. Select a template or load an existing file.
                2. Edit your hotbars in the main editor.
                3. Click 'Save' to download the updated hotbars.nbt file.
                
                (Actual instructions page TODO)
            """.trimIndent(),
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = MaterialTheme.typography.bodyMedium.fontSize * 1.5
        )
    }
}