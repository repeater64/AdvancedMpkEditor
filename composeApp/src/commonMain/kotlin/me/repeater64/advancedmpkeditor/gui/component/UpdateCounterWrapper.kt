package me.repeater64.advancedmpkeditor.gui.component

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class UpdateCounterWrapper {
    var numUpdates by mutableStateOf(0)
}