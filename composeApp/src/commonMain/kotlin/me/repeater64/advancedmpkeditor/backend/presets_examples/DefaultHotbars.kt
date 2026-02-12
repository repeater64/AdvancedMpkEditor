package me.repeater64.advancedmpkeditor.backend.presets_examples

import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.AirItem
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.CommandBlockItem
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbar
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbars

object DefaultHotbars {
    val exampleTemplate
        get() = SavedHotbars(listOf(
        SavedHotbar(listOf(
            EndEnterHotbarSorted.barrel, AirItem(), AirItem(), AirItem(), AirItem(), AirItem(), AirItem(), AirItem(), CommandBlockItem()
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