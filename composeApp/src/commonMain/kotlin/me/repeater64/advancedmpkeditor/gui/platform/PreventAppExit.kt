package me.repeater64.advancedmpkeditor.gui.platform

import androidx.compose.runtime.Composable

@Composable
expect fun PreventAppExit(hasUnsavedChanges: Boolean)