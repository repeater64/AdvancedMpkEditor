package me.repeater64.advancedmpkeditor.backend.presets_examples

import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.AirItem
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.CommandBlockItem
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbar
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbars

object DefaultHotbars {
    val exampleTemplate
        get() = SavedHotbars(hashMapOf(
        0 to SavedHotbar(arrayOf(
            EndEnterHotbarSorted.barrel, AirItem(), AirItem(), AirItem(), AirItem(), AirItem(), AirItem(), AirItem(), CommandBlockItem()
        )),
        1 to emptyHotbar(),
        2 to emptyHotbar(),
        3 to emptyHotbar(),
        4 to emptyHotbar(),
        5 to emptyHotbar(),
        6 to emptyHotbar(),
        7 to emptyHotbar(),
        8 to emptyHotbar(),
    ))
    val blankTemplate
        get() = SavedHotbars(hashMapOf(
        0 to SavedHotbar(arrayOf(
            BlankBarrel.barrel, AirItem(), AirItem(), AirItem(), AirItem(), AirItem(), AirItem(), AirItem(), CommandBlockItem()
        )),
        1 to emptyHotbar(),
        2 to emptyHotbar(),
        3 to emptyHotbar(),
        4 to emptyHotbar(),
        5 to emptyHotbar(),
        6 to emptyHotbar(),
        7 to emptyHotbar(),
        8 to emptyHotbar(),
    ))
}