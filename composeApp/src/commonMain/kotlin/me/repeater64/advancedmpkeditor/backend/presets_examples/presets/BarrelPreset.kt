package me.repeater64.advancedmpkeditor.backend.presets_examples.presets

import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.BarrelItem
import me.repeater64.advancedmpkeditor.backend.presets_examples.BlankBarrel
import me.repeater64.advancedmpkeditor.backend.presets_examples.barrels.EndEnterHotbarSortedBarrel

enum class BarrelPreset(val displayName: String, val barrelGetter: () -> BarrelItem) {
    END_ENTER_HOTBAR_SORTED("End Enter (Hotbar Sorted)", { EndEnterHotbarSortedBarrel.barrel }),
    TEMP_BLANK("Temp Blank Barrel", { BlankBarrel.barrel }),
}