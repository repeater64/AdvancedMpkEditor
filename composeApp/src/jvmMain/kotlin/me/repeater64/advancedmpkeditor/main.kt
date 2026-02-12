package me.repeater64.advancedmpkeditor

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.BootsSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.ChestplateSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.FixedSlotsData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.HelmetSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.LeggingsSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.OffhandSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.item.EnchantedBootsItem
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.DifficultyOption
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.GamemodeOption
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.PracticeTypeOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.AirItem
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.BarrelItem
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.CommandBlockItem
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbar
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbars
import me.repeater64.advancedmpkeditor.backend.*
import me.repeater64.advancedmpkeditor.backend.data_object.AllCommandsSettings
import me.repeater64.advancedmpkeditor.backend.data_object.fire_res.FireResSettings
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HealthHungerOption
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HealthHungerSettings
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HealthOption
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HungerOption
import me.repeater64.advancedmpkeditor.backend.data_object.item.FireResItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.SplashFireResItem
import me.repeater64.advancedmpkeditor.backend.data_object.junk.JunkSettings
import me.repeater64.advancedmpkeditor.backend.data_object.random_slot.RandomSlotOptionsSet
import me.repeater64.advancedmpkeditor.backend.data_object.random_slot.RandomSlotsData
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.backend.presets_examples.DefaultHotbars
import me.repeater64.advancedmpkeditor.backend.presets_examples.availableItem
import me.repeater64.advancedmpkeditor.backend.presets_examples.condition
import me.repeater64.advancedmpkeditor.backend.presets_examples.emptyItem
import me.repeater64.advancedmpkeditor.backend.presets_examples.hotbarSlot
import me.repeater64.advancedmpkeditor.backend.presets_examples.invCondition
import me.repeater64.advancedmpkeditor.backend.presets_examples.invSlot
import me.repeater64.advancedmpkeditor.backend.presets_examples.item
import me.repeater64.advancedmpkeditor.backend.presets_examples.itemList
import me.repeater64.advancedmpkeditor.backend.presets_examples.optionList
import me.repeater64.advancedmpkeditor.backend.presets_examples.rawItem
import me.repeater64.advancedmpkeditor.gui.MainApp
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer
import kotlin.use

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        alwaysOnTop = false,
        title = "Advanced MPK Editor",
    ) {
        val manager = remember { JvmHotbarFileManager() }
        MainApp(manager)
    }
}

fun mainTemp() {
    val savedHotbars = DefaultHotbars.exampleTemplate


    val savedHotbarsLoaded = loadNbtFile("hotbar.nbt")
//    prettyPrintDataClass(savedHotbarsLoaded.toString())

    println(savedHotbars == savedHotbarsLoaded)


//    println("Available slots: ${fixedSlotsData.numAvailableForRandomInvSlots()}")
//    println("Max random slots: ${randomSlotsData.totalMaxNumStacks()}")

    saveNbtFile("hotbar.nbt", savedHotbars)
    saveNbtFile("hotbarRecreated.nbt", savedHotbarsLoaded)

//    quicktest()
}

//fun quicktest() {
//    val rawCommand = "execute if score @p m matches 12..15 if score @p h matches 1 unless score @p h matches 2..3 unless score @p h matches 4..6 unless score @p h matches 7..8 unless score @p h matches 9 run replaceitem entity @p hotbar.3 minecraft:birch_boat 1"
//
//    val simplified = CommandsManager.simplifyCommand(rawCommand)
//    println("Original:   $rawCommand")
//    println("Simplified: $simplified")
//}

fun saveNbtFile(path: String, savedHotbars: SavedHotbars) {
    val okioPath = path.toPath()

    FileSystem.SYSTEM.sink(okioPath).buffer().use { sink ->
        // Pass the serializer, the object to save, and the sink
        savedHotbars.encodeToNbtSink(sink)
    }
}

fun loadNbtFile(path: String): SavedHotbars {
    val okioPath = path.toPath()

    return FileSystem.SYSTEM.source(okioPath).buffer().use { source ->
        // We pass the built-in serializer for NbtCompound here
        SavedHotbars.decodeFromNbtSource(source)
    }
}