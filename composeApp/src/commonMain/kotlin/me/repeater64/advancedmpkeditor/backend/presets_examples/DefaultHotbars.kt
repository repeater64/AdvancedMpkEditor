package me.repeater64.advancedmpkeditor.backend.presets_examples

import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.AirItem
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.CommandBlockItem
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbar
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbars
import me.repeater64.advancedmpkeditor.backend.presets_examples.barrels.BastionBarrel
import me.repeater64.advancedmpkeditor.backend.presets_examples.barrels.BlindMaybePrecraftedBarrel
import me.repeater64.advancedmpkeditor.backend.presets_examples.barrels.BlindPrecraftedBarrel
import me.repeater64.advancedmpkeditor.backend.presets_examples.barrels.EndEnterHotbarSortedBarrel
import me.repeater64.advancedmpkeditor.backend.presets_examples.barrels.EndEnterHotbarUnsortedBarrel
import me.repeater64.advancedmpkeditor.backend.presets_examples.barrels.EyeSpyNotPrecraftedBarrel
import me.repeater64.advancedmpkeditor.backend.presets_examples.barrels.EyeSpyPrecraftedBarrel
import me.repeater64.advancedmpkeditor.backend.presets_examples.barrels.FortressBarrel

object DefaultHotbars {
    val exampleTemplate
        get() = SavedHotbars(listOf(
        SavedHotbar(listOf(
            BastionBarrel.barrel, FortressBarrel.barrel, BlindPrecraftedBarrel.barrel, BlindMaybePrecraftedBarrel.barrel, EyeSpyNotPrecraftedBarrel.barrel, EyeSpyPrecraftedBarrel.barrel, EndEnterHotbarSortedBarrel.barrel, EndEnterHotbarUnsortedBarrel.barrel, CommandBlockItem()
        )),
        emptyHotbar(),
        emptyHotbar(),
        emptyHotbar(),
        emptyHotbar(),
        emptyHotbar(),
        emptyHotbar(),
        emptyHotbar(),
        emptyHotbar(),
    ))
    val blankTemplate
        get() = SavedHotbars(listOf(
        SavedHotbar(listOf(
            BlankBarrel.barrel, AirItem(), AirItem(), AirItem(), AirItem(), AirItem(), AirItem(), AirItem(), CommandBlockItem()
        )),
        emptyHotbar(),
        emptyHotbar(),
        emptyHotbar(),
        emptyHotbar(),
        emptyHotbar(),
        emptyHotbar(),
        emptyHotbar(),
        emptyHotbar(),
    ))
}