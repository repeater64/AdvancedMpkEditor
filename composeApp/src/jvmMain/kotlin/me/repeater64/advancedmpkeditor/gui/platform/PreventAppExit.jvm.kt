package me.repeater64.advancedmpkeditor.gui.platform

import androidx.compose.runtime.Composable

@Composable
actual fun PreventAppExit(hasUnsavedChanges: Boolean) {
    // This behaviour is implemented on web only
}