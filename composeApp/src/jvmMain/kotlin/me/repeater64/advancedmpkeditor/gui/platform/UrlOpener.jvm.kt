package me.repeater64.advancedmpkeditor.gui.platform

import java.awt.Desktop
import java.net.URI

actual fun openUrl(url: String) {
    val desktop = if (Desktop.isDesktopSupported()) Desktop.getDesktop() else null
    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
        try {
            desktop.browse(URI(url))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}