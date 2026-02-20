package me.repeater64.advancedmpkeditor.backend.presets_examples.presets

import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.BarrelItem
import me.repeater64.advancedmpkeditor.backend.presets_examples.BlankBarrel
import me.repeater64.advancedmpkeditor.backend.presets_examples.barrels.EndEnterHotbarSortedBarrel
import me.repeater64.advancedmpkeditor.backend.presets_examples.barrels.EndEnterHotbarUnsortedBarrel
import me.repeater64.advancedmpkeditor.backend.presets_examples.barrels.EyeSpyNotPrecraftedBarrel
import me.repeater64.advancedmpkeditor.backend.presets_examples.barrels.EyeSpyPrecraftedBarrel

enum class BarrelPreset(val displayName: String, val barrelGetter: () -> BarrelItem) {
    END_ENTER_HOTBAR_SORTED("End Enter (Hotbar Sorted)", { EndEnterHotbarSortedBarrel.barrel }),
    END_ENTER_HOTBAR_UNSORTED("End Enter (Hotbar Unsorted)", { EndEnterHotbarUnsortedBarrel.barrel }),
    EYE_SPY_PRECRAFTED("Eye Spy (Precrafted)", { EyeSpyPrecraftedBarrel.barrel }),
    EYE_SPY_NOT_PRECRAFTED("Eye Spy (Not Precrafted)", { EyeSpyNotPrecraftedBarrel.barrel }),
}