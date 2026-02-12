package me.repeater64.advancedmpkeditor.gui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SimpleTextField(modifier: Modifier = Modifier, label: String, currentText: String, textChanged: (String) -> Any) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        OutlinedTextField(
            value = currentText,
            onValueChange = {textChanged(it)},
            readOnly = false,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = OutlinedTextFieldDefaults.colors().unfocusedTextColor,
                focusedContainerColor = OutlinedTextFieldDefaults.colors().unfocusedContainerColor,
                focusedLabelColor = OutlinedTextFieldDefaults.colors().unfocusedLabelColor,
                focusedBorderColor = MaterialTheme.colorScheme.outline,
            )
        )
    }
}