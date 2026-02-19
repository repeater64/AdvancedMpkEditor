package me.repeater64.advancedmpkeditor

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application


import me.repeater64.advancedmpkeditor.gui.MainApp

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

//fun mainTemp() {
//    val savedHotbars = DefaultHotbars.exampleTemplate
//
//
//    val savedHotbarsLoaded = loadNbtFile("hotbar.nbt")
////    prettyPrintDataClass(savedHotbarsLoaded.toString())
//
//    println(savedHotbars == savedHotbarsLoaded)
//
//
////    println("Available slots: ${fixedSlotsData.numAvailableForRandomInvSlots()}")
////    println("Max random slots: ${randomSlotsData.totalMaxNumStacks()}")
//
//    saveNbtFile("hotbar.nbt", savedHotbars)
//    saveNbtFile("hotbarRecreated.nbt", savedHotbarsLoaded)
//
////    quicktest()
//}

//fun quicktest() {
//    val rawCommand = "execute if score @p m matches 12..15 if score @p h matches 1 unless score @p h matches 2..3 unless score @p h matches 4..6 unless score @p h matches 7..8 unless score @p h matches 9 run replaceitem entity @p hotbar.3 minecraft:birch_boat 1"
//
//    val simplified = CommandsManager.simplifyCommand(rawCommand)
//    println("Original:   $rawCommand")
//    println("Simplified: $simplified")
//}
//
//fun saveNbtFile(path: String, savedHotbars: SavedHotbars) {
//    val okioPath = path.toPath()
//
//    FileSystem.SYSTEM.sink(okioPath).buffer().use { sink ->
//        // Pass the serializer, the object to save, and the sink
//        savedHotbars.encodeToNbtSink(sink)
//    }
//}
//
//fun loadNbtFile(path: String): SavedHotbars {
//    val okioPath = path.toPath()
//
//    return FileSystem.SYSTEM.source(okioPath).buffer().use { source ->
//        // We pass the built-in serializer for NbtCompound here
//        SavedHotbars.decodeFromNbtSource(source)
//    }
//}