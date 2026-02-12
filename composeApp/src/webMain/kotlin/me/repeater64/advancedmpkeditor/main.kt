package me.repeater64.advancedmpkeditor

import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import me.repeater64.advancedmpkeditor.gui.MainApp

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport() {
        val manager = remember { JsHotbarFileManager() }
        MainApp(manager)
    }
}