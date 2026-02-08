package me.repeater64.advancedmpkeditor

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.BootsSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.ChestplateSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.FixedSlotsData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.HelmetSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.HotbarSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.InventorySlotData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.LeggingsSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.OffhandSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.item.DontReplaceMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.EnchantedBootsItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.ForcedEmptyMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.SimpleMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.DifficultyOption
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.GamemodeOption
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.PracticeTypeOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.RandomiserCondition
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.AirItem
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.BarrelItem
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.CommandBlockItem
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbar
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbars
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

fun item(id: String, weight: Int = 1, amount: Int = 1, label: String? = null, conditions: List<RandomiserCondition> = emptyList()): WeightedOption<MinecraftItem> {
    return WeightedOption(SimpleMinecraftItem("minecraft:$id", amount), weight, label, conditions)
}

fun condition(label: String) = listOf(RandomiserCondition(label, false))
fun invCondition(label: String) = listOf(RandomiserCondition(label, true))
fun hotbarSlot(pos: Int, items: WeightedOptionList<MinecraftItem>) = HotbarSlotData(pos, items)
fun invSlot(pos: Int, items: WeightedOptionList<MinecraftItem>) = InventorySlotData(pos, false, items)
fun emptyInvSlot(pos: Int) = InventorySlotData(pos, true, WeightedOptionList(mutableListOf()))
fun emptyItem(weight: Int = 1, label: String? = null, conditions: List<RandomiserCondition> = emptyList() ) = WeightedOption<MinecraftItem>(ForcedEmptyMinecraftItem(), weight, label, conditions)
fun availableItem(weight: Int = 1, label: String? = null, conditions: List<RandomiserCondition> = emptyList() ) = WeightedOption<MinecraftItem>(DontReplaceMinecraftItem(), weight, label, conditions)

fun optionList(vararg options: WeightedOption<MinecraftItem>) = WeightedOptionList(options.toMutableList())

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
                item("stone_axe", 1),
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
                item("birch_boat", 4),
                item("acacia_boat", 6),
                item("dark_oak_boat", 2),
                item("jungle_boat", 1),
                item("spruce_boat", 2),
                item("glowstone", 10, label="anchors"),
                item("glowstone", 10, label="anchors"),
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
                item("obsidian", 2, 1),
                item("obsidian", 2, 3),
                emptyItem(3)
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
                item("coal", conditions=listOf(RandomiserCondition("oak_boat"), RandomiserCondition("eye", true))),
                availableItem()
            ))
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

//    val savedHotbars = loadNbtFile("hotbar.nbt")
    prettyPrintDataClass(savedHotbars.toString())


    saveNbtFile("hotbar.nbt", savedHotbars)
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