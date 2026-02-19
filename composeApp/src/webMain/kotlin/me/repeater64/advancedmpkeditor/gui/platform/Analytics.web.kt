package me.repeater64.advancedmpkeditor.gui.platform

import kotlinx.browser.window
import kotlin.js.json

/**
 * Triggers a custom GoatCounter event
 */
internal actual fun trackEvent(eventPath: String, eventTitle: String) {
    val w = window.asDynamic()

    if (w.goatcounter != null && w.goatcounter.count != null) {

        w.goatcounter.count(
            json(
                "path" to eventPath,
                "title" to eventTitle,
                "event" to true
            )
        )
    } else {
        console.warn("GoatCounter not found. Event '$eventTitle' not tracked.")
    }
}