package me.repeater64.advancedmpkeditor

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application


import me.repeater64.advancedmpkeditor.gui.MainApp

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        alwaysOnTop = false,
        title = "Advanced MPK Editor",
    ) {
        val manager = remember { JvmHotbarFileManager() }
        MainApp(manager)
    }
}