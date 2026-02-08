package me.repeater64.advancedmpkeditor

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
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.RandomiserCondition
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.AirItem
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.BarrelItem
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.CommandBlockItem
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbar
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbars
import me.repeater64.advancedmpkeditor.backend.*
import me.repeater64.advancedmpkeditor.backend.data_object.item.ForcedEmptyMinecraftItem
import me.repeater64.advancedmpkeditor.util.prettyPrintDataClass
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer
import kotlin.use

fun mainTemp() = application {
    Window(
        onCloseRequest = ::exitApplication,
        alwaysOnTop = true,
        title = "advancedmpkeditor",
    ) {
        App()
    }
}

fun main() {
    val fixedSlotsData = FixedSlotsData(
        OffhandSlotData(optionList(
            item("bread", 3, 3),
            item("cooked_porkchop", 2, 1),
            item("rotten_flesh", 2, 4),
            emptyItem(1)
        )),
        arrayOf(
            hotbarSlot(0, optionList(
                item("iron_axe", 2),
                item("stone_axe", 1, label="stone_axe"),
            )),
            hotbarSlot(1, optionList(
                item("iron_pickaxe", 8),
                item("diamond_pickaxe", 1),
            )),
            hotbarSlot(2, optionList(
                item("iron_shovel", 1, conditions = invCondition("anchors")),
                item("stone_shovel", 1, conditions = invCondition("anchors")),
                item("respawn_anchor", 1, 2, conditions = condition("anchors")),
                item("respawn_anchor", 1, 3, conditions = condition("anchors")),
            )),
            hotbarSlot(3, optionList(
                item("oak_boat", 12, label="oak_boat"),
                item("birch_boat", 4, label="oak_boat"),
                item("acacia_boat", 6),
                item("dark_oak_boat", 2),
                item("jungle_boat", 1),
                item("spruce_boat", 2),
                item("glowstone", 10, 3, label="anchors"),
                item("glowstone", 10, 4, label="anchors"),
            )),
            hotbarSlot(4, optionList(
                item("soul_sand", 1, 55),
                item("soul_sand", 1, 47),
                item("soul_sand", 1, 33),
                item("soul_sand", 1, 28),
                item("soul_sand", 1, 19),
            )),
            hotbarSlot(5, optionList(
                item("nether_bricks", 2, 15),
                item("nether_bricks", 2, 18),
                item("nether_bricks", 3, 23),
                item("nether_bricks", 3, 26),
                item("nether_bricks", 3, 31),
                item("nether_bricks", 2, 37),
                item("nether_bricks", 1, 42),
                item("nether_bricks", 1, 55),
            )),
            hotbarSlot(6, optionList(
                item("obsidian", 2, 1, label="eye"),
                item("obsidian", 2, 3),
                emptyItem(3, label="eye")
            )),
            hotbarSlot(7, optionList(
                item("ender_pearl", amount = 13)
            )),
            hotbarSlot(8, optionList(
                item("ender_eye", label="eye"),
                emptyItem()
            )),
        ),
        arrayOf(
            invSlot(0, optionList(item("fire_charge", amount=23))),
            invSlot(1, optionList(
                item("coal", conditions=listOf(RandomiserCondition("oak_boat"), RandomiserCondition("eye", false), RandomiserCondition("stone_axe", true))),
                availableItem()
            )),
            invSlot(2, optionList(availableItem())),
            invSlot(3, optionList(availableItem())),
            invSlot(4, optionList(availableItem())),
            invSlot(5, optionList(availableItem())),
            invSlot(6, optionList(availableItem())),
            invSlot(7, optionList(availableItem())),
            invSlot(8, optionList(availableItem())),
            invSlot(9, optionList(availableItem())),
            invSlot(10, optionList(availableItem())),
            invSlot(11, optionList(availableItem())),
            invSlot(12, optionList(availableItem())),
            invSlot(13, optionList(availableItem())),
            invSlot(14, optionList(availableItem())),
            invSlot(15, optionList(availableItem())),
            invSlot(16, optionList(availableItem())),
            invSlot(17, optionList(availableItem())),
            invSlot(18, optionList(availableItem())),
            invSlot(19, optionList(availableItem())),
            invSlot(20, optionList(availableItem())),
            invSlot(21, optionList(availableItem())),
            invSlot(22, optionList(availableItem())),
            invSlot(23, optionList(availableItem())),
            invSlot(24, optionList(availableItem())),
            invSlot(25, optionList(availableItem())),
            invSlot(26, optionList(availableItem()))
        ),
        HelmetSlotData(optionList(
            item("golden_helmet", 6),
            item("iron_helmet", 1),
            emptyItem(8)
        )),
        ChestplateSlotData(optionList(
            item("golden_chestplate", 6),
            item("iron_chestplate", 1),
            emptyItem(8)
        )),
        LeggingsSlotData(optionList(
            item("golden_leggings", 6),
            item("iron_leggings", 1),
            emptyItem(8)
        )),
        BootsSlotData(optionList(
            item("golden_boots", 5),
            item("iron_boots", 1),
            emptyItem(12),
            WeightedOption(EnchantedBootsItem(true, 1), 5),
            WeightedOption(EnchantedBootsItem(true, 2), 5),
            WeightedOption(EnchantedBootsItem(true, 3), 5),
            WeightedOption(EnchantedBootsItem(false, 1), 5),
            WeightedOption(EnchantedBootsItem(false, 2), 5),
            WeightedOption(EnchantedBootsItem(false, 3), 5),
        )),
    )

    val savedHotbars = SavedHotbars(hashMapOf(
        0 to SavedHotbar(arrayOf(
            BarrelItem("Test Barrel", PracticeTypeOption.END_ENTER, GamemodeOption.CREATIVE, DifficultyOption.EASY, fixedSlotsData),
            AirItem(),AirItem(),AirItem(),AirItem(),AirItem(),AirItem(),AirItem(),
            CommandBlockItem()
        )),
        1 to SavedHotbar.emptyHotbar(),
        2 to SavedHotbar.emptyHotbar(),
        3 to SavedHotbar.emptyHotbar(),
        4 to SavedHotbar.emptyHotbar(),
        5 to SavedHotbar.emptyHotbar(),
        6 to SavedHotbar.emptyHotbar(),
        7 to SavedHotbar.emptyHotbar(),
        8 to SavedHotbar.emptyHotbar(),
    ))

    val savedHotbarsLoaded = loadNbtFile("hotbar.nbt")
    prettyPrintDataClass(savedHotbarsLoaded.toString())

    println(savedHotbars == savedHotbarsLoaded)


    saveNbtFile("hotbar.nbt", savedHotbars)
    saveNbtFile("hotbarRecreated.nbt", savedHotbarsLoaded)
}

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