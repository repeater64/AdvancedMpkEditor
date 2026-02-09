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
import me.repeater64.advancedmpkeditor.backend.data_object.AllCommandsSettings
import me.repeater64.advancedmpkeditor.backend.data_object.item.FireResItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.ForcedEmptyMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.SplashFireResItem
import me.repeater64.advancedmpkeditor.backend.data_object.junk.JunkSettings
import me.repeater64.advancedmpkeditor.backend.data_object.random_slot.RandomSlotOptionsSet
import me.repeater64.advancedmpkeditor.backend.data_object.random_slot.RandomSlotsData
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
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
                item("iron_shovel", 1, label="0a"),
                item("stone_shovel", 1, label="0a"),
                item("respawn_anchor", 2, 1, label="1a"),
                item("respawn_anchor", 3, 2, label="2a"),
                item("respawn_anchor", 2, 3, label="3a"),
                item("respawn_anchor", 1, 4, label="4a"),
            )),
            hotbarSlot(3, optionList(
                item("oak_boat", 12, conditions=condition("0a")),
                item("birch_boat", 4, conditions=condition("0a")),
                item("acacia_boat", 6, conditions=condition("0a")),
                item("dark_oak_boat", 2, conditions=condition("0a")),
                item("jungle_boat", 1, conditions=condition("0a")),
                item("spruce_boat", 2, conditions=condition("0a")),
                item("glowstone", 3, 1, conditions=condition("1a")),
                item("glowstone", 1, 2, conditions=condition("1a")),
                item("glowstone", 3, 2, conditions=condition("2a")),
                item("glowstone", 1, 3, conditions=condition("2a")),
                item("glowstone", 3, 3, conditions=condition("3a")),
                item("glowstone", 1, 4, conditions=condition("3a")),
                item("glowstone", 3, 4, conditions=condition("4a")),
                item("glowstone", 1, 5, conditions=condition("4a")),
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
            invSlot(0, optionList(item("fire_charge", amount=23), item("fire_charge", amount=15))),
            invSlot(1, optionList(availableItem())),
            invSlot(2, optionList(availableItem())),
            invSlot(3, optionList(availableItem())),
            invSlot(4, optionList(availableItem())),
            invSlot(5, optionList(availableItem())),
            invSlot(6, optionList(item("ender_pearl", amount=16))),
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

    val randomSlotsData = RandomSlotsData(
        listOf(
            RandomSlotOptionsSet(
                "Beds",
                WeightedOptionList(mutableListOf(
                    itemList(rawItem("white_bed", 5), weight=1, conditions=condition("0a")),
                    itemList(rawItem("white_bed", 6), weight=3, conditions=condition("0a")),
                    itemList(rawItem("white_bed", 7), weight=2, conditions=condition("0a")),
                    itemList(rawItem("white_bed", 8), weight=1, conditions=condition("0a")),

                    itemList(rawItem("white_bed", 4), weight=2, conditions=condition("1a")),
                    itemList(rawItem("white_bed", 5), weight=8, conditions=condition("1a")),
                    itemList(rawItem("white_bed", 6), weight=6, conditions=condition("1a")),
                    itemList(rawItem("white_bed", 7), weight=2, conditions=condition("1a")),
                    itemList(rawItem("white_bed", 8), weight=1, conditions=condition("1a")),

                    itemList(rawItem("white_bed", 3), weight=1, conditions=condition("2a")),
                    itemList(rawItem("white_bed", 4), weight=3, conditions=condition("2a")),
                    itemList(rawItem("white_bed", 5), weight=2, conditions=condition("2a")),
                    itemList(rawItem("white_bed", 6), weight=1, conditions=condition("2a")),

                    itemList(rawItem("white_bed", 2), weight=2, conditions=condition("3a")),
                    itemList(rawItem("white_bed", 3), weight=4, conditions=condition("3a")),
                    itemList(rawItem("white_bed", 4), weight=3, conditions=condition("3a")),
                    itemList(rawItem("white_bed", 5), weight=2, conditions=condition("3a")),
                    itemList(rawItem("white_bed", 6), weight=1, conditions=condition("3a")),

                    itemList(rawItem("white_bed", 1), weight=1, conditions=condition("4a")),
                    itemList(rawItem("white_bed", 2), weight=3, conditions=condition("4a")),
                    itemList(rawItem("white_bed", 3), weight=2, conditions=condition("4a")),
                    itemList(rawItem("white_bed", 4), weight=1, conditions=condition("4a")),
                ))
            ),
            RandomSlotOptionsSet(
                "Bow",
                WeightedOptionList(mutableListOf(
                    itemList(rawItem("bow"), weight=3),
                    itemList(rawItem("crossbow"), weight=2),
                    itemList(availableItem().option, weight=1)
                ))
            ),
            RandomSlotOptionsSet(
                "Arrows",
                WeightedOptionList(mutableListOf(
                    itemList(rawItem("arrow", 6), weight=3),
                    itemList(rawItem("arrow", 12), weight=2),
                    itemList(rawItem("arrow", 20), rawItem("spectral_arrow", 13), weight=2),
                    itemList(rawItem("arrow", 34), weight=1),
                    itemList(rawItem("arrow", 50), weight=1),
                    itemList(rawItem("arrow", 64), weight=2),
                    itemList(rawItem("arrow", 64), rawItem("spectral_arrow", 30), weight=1),
                    itemList(availableItem().option, weight=3)
                ))
            ),
            RandomSlotOptionsSet(
                "Spare Building Blocks",
                WeightedOptionList(mutableListOf(
                    itemList(rawItem("gravel", 110), rawItem("oak_planks", 4), rawItem("crying_obsidian", 6), rawItem("soul_sand", 64), weight=5),
                    itemList(rawItem("gravel", 64), rawItem("oak_planks", 30), rawItem("crying_obsidian", 5), weight=2),
                    itemList(rawItem("gravel", 36), rawItem("oak_planks", 3), rawItem("crying_obsidian", 24), weight=1),
                    itemList(rawItem("gravel", 144), rawItem("oak_planks", 5), rawItem("crying_obsidian", 31), weight=1),
                    itemList(rawItem("gravel", 40), rawItem("oak_planks", 3), rawItem("crying_obsidian", 4), rawItem("netherrack", 53), weight=3),
                    itemList(rawItem("gravel", 53), rawItem("oak_planks", 4), rawItem("crying_obsidian", 3), rawItem("netherrack", 64), rawItem("netherrack", 30), weight=1),
                ))
            ),
            RandomSlotOptionsSet(
                "Sticks",
                WeightedOptionList(mutableListOf(
                    itemList(rawItem("stick", 1), weight=1),
                    itemList(rawItem("stick", 2), weight=1),
                    itemList(availableItem().option, weight=1)
                ))
            ),
            RandomSlotOptionsSet(
                "Bucket",
                WeightedOptionList(mutableListOf(
                    itemList(rawItem("lava_bucket"), weight=6),
                    itemList(rawItem("water_bucket"), weight=1),
                    itemList(availableItem().option, weight=2)
                ))
            ),
            RandomSlotOptionsSet(
                "Boat",
                WeightedOptionList(mutableListOf(
                    itemList(rawItem("oak_boat"), weight=12, conditions=invCondition("0a")),
                    itemList(rawItem("birch_boat"), weight=4, conditions=invCondition("0a")),
                    itemList(rawItem("acacia_boat"), weight=6, conditions=invCondition("0a")),
                    itemList(rawItem("dark_oak_boat"), weight=2, conditions=invCondition("0a")),
                    itemList(rawItem("jungle_boat"), weight=1, conditions=invCondition("0a")),
                    itemList(rawItem("spruce_boat"), weight=2, conditions=invCondition("0a")),
                    itemList(availableItem().option, conditions=condition("0a"))
                ))
            ),
            RandomSlotOptionsSet(
                "Shovel",
                WeightedOptionList(mutableListOf(
                    itemList(rawItem("iron_shovel"), weight=1, conditions=invCondition("0a")),
                    itemList(rawItem("stone_shovel"), weight=1, conditions=invCondition("0a")),
                    itemList(availableItem().option, conditions=condition("0a"))
                ))
            ),
        )
    )

    val junkSettings = JunkSettings(true, true, listOf(
        rawItem("string", 2),
        rawItem("iron_ingot", 4),
        rawItem("iron_nugget", 40),
        SplashFireResItem(),
        rawItem("gold_ingot", 2),
        rawItem("golden_boots", 1),
        rawItem("leather", 64),
        SplashFireResItem(),
        rawItem("glowstone_dust", 3),
        rawItem("cracked_stone_bricks", 1),
        FireResItem(),
        rawItem("mossy_stone_bricks", 1),
        rawItem("tnt", 1),
        FireResItem(),
        rawItem("leather", 64),
        rawItem("nether_brick", 3),
        rawItem("dirt", 4),
        rawItem("nether_brick_fence", 1),
        EnchantedBootsItem(true, 1),
    ))

    println(CommandsManager.generateCommands(AllCommandsSettings(fixedSlotsData, randomSlotsData, junkSettings)).first.joinToString("\n"))

    val savedHotbars = SavedHotbars(hashMapOf(
        0 to SavedHotbar(arrayOf(
            AirItem(),AirItem(),AirItem(),AirItem(),AirItem(),AirItem(),
            BarrelItem("Test Barrel", PracticeTypeOption.END_ENTER, GamemodeOption.SURVIVAL, DifficultyOption.EASY, fixedSlotsData, randomSlotsData, junkSettings),
            AirItem(),
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
//    prettyPrintDataClass(savedHotbarsLoaded.toString())

    println(savedHotbars == savedHotbarsLoaded)


    println("Available slots: ${fixedSlotsData.numAvailableForRandomInvSlots()}")
    println("Max random slots: ${randomSlotsData.totalMaxNumStacks()}")

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