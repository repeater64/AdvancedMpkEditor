package me.repeater64.advancedmpkeditor.gui.screens

import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbars

sealed class Screen {
    data object Home : Screen()
    data object Import : Screen()
    data object Instructions : Screen()
    data object Credits : Screen()
    data class Editor(val hotbars: SavedHotbars) : Screen()
}