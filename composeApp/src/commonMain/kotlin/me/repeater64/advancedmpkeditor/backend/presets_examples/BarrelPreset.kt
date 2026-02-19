package me.repeater64.advancedmpkeditor.backend.presets_examples

import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.BarrelItem

enum class BarrelPreset(val displayName: String, val barrelGetter: () -> BarrelItem) {
    END_ENTER_HOTBAR_SORTED("End Enter (Hotbar Sorted)", { EndEnterHotbarSorted.barrel }),
    TEMP_BLANK("Temp Blank Barrel", { BlankBarrel.barrel }),
}