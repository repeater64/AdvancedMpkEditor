package me.repeater64.advancedmpkeditor.gui.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import kotlinx.browser.window
import org.w3c.dom.events.Event

@Composable
actual fun PreventAppExit(hasUnsavedChanges: Boolean) {
    DisposableEffect(hasUnsavedChanges) {
        if (!hasUnsavedChanges) return@DisposableEffect onDispose { }

        val onBeforeUnload = { e: Event ->
            e.preventDefault()
            e.asDynamic().returnValue = ""
        }

        window.addEventListener("beforeunload", onBeforeUnload)

        onDispose {
            window.removeEventListener("beforeunload", onBeforeUnload)
        }
    }
}