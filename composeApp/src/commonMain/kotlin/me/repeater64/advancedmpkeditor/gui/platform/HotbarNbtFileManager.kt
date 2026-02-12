package me.repeater64.advancedmpkeditor.gui.platform

import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbars

// commonMain
interface HotbarNbtFileManager {
    // JVM: Saves to path. JS: Triggers browser download.
    fun saveHotbars(hotbars: SavedHotbars, fileName: String = "hotbar.nbt")

    // JVM: Reads from path. JS: Triggers file picker dialog.
    suspend fun loadHotbars(): SavedHotbars?
}