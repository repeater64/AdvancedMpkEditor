package me.repeater64.advancedmpkeditor.gui.platform

import kotlinx.browser.window

actual fun openUrl(url: String) {
    window.open(url, "_blank")
}