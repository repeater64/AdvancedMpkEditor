package me.repeater64.advancedmpkeditor.backend.presets_examples.presets

import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.BarrelItem
import me.repeater64.advancedmpkeditor.backend.presets_examples.BlankBarrel
import me.repeater64.advancedmpkeditor.backend.presets_examples.barrels.BastionBarrel
import me.repeater64.advancedmpkeditor.backend.presets_examples.barrels.BlindMaybePrecraftedBarrel
import me.repeater64.advancedmpkeditor.backend.presets_examples.barrels.BlindNotPrecraftedBarrel
import me.repeater64.advancedmpkeditor.backend.presets_examples.barrels.BlindPrecraftedBarrel
import me.repeater64.advancedmpkeditor.backend.presets_examples.barrels.EndEnterHotbarSortedBarrel
import me.repeater64.advancedmpkeditor.backend.presets_examples.barrels.EndEnterHotbarUnsortedBarrel
import me.repeater64.advancedmpkeditor.backend.presets_examples.barrels.EyeSpyNotPrecraftedBarrel
import me.repeater64.advancedmpkeditor.backend.presets_examples.barrels.EyeSpyPrecraftedBarrel
import me.repeater64.advancedmpkeditor.backend.presets_examples.barrels.FortressBarrel

enum class BarrelPreset(val displayName: String, val barrelGetter: () -> BarrelItem) {
    END_ENTER_HOTBAR_SORTED("End Enter (Hotbar Sorted)", { EndEnterHotbarSortedBarrel.barrel }),
    END_ENTER_HOTBAR_UNSORTED("End Enter (Hotbar Unsorted)", { EndEnterHotbarUnsortedBarrel.barrel }),
    EYE_SPY_PRECRAFTED("Eye Spy (Precrafted)", { EyeSpyPrecraftedBarrel.barrel }),
    EYE_SPY_NOT_PRECRAFTED("Eye Spy (Not Precrafted)", { EyeSpyNotPrecraftedBarrel.barrel }),
    BLIND_PRECRAFTED("Blind (Always Precrafted)", { BlindPrecraftedBarrel.barrel }),
    BLIND_NOT_PRECRAFTED("Blind (Not Precrafted)", { BlindNotPrecraftedBarrel.barrel }),
    BLIND_MAYBE_PRECRAFTED("Blind (50% Chance Precrafted)", { BlindMaybePrecraftedBarrel.barrel }),
    FORTRESS("Fortress", { FortressBarrel.barrel }),
    BASTION("Bastion", { BastionBarrel.barrel }),
}