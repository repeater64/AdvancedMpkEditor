package me.repeater64.advancedmpkeditor.gui.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbars

@Composable
expect fun FileDropZone(
    modifier: Modifier = Modifier,
    onFileDropped: (SavedHotbars) -> Unit,
    content: @Composable () -> Unit
)