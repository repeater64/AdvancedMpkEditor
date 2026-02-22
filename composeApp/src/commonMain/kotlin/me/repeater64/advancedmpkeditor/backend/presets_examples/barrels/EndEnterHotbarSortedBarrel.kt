package me.repeater64.advancedmpkeditor.backend.presets_examples.barrels

import me.repeater64.advancedmpkeditor.backend.data_object.fire_res.FireResSettings
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.BootsSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.ChestplateSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.FixedSlotsData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.HelmetSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.LeggingsSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.OffhandSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HealthHungerOption
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HealthHungerSettings
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HealthOption
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HungerOption
import me.repeater64.advancedmpkeditor.backend.data_object.item.EnchantedBootsItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.FireResItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.ForcePerchPotionItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.SoulSpeedBookItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.SplashFireResItem
import me.repeater64.advancedmpkeditor.backend.data_object.junk.JunkSettings
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.DifficultyOption
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.GamemodeOption
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.PracticeTypeOption
import me.repeater64.advancedmpkeditor.backend.data_object.random_slot.RandomSlotOptionsSet
import me.repeater64.advancedmpkeditor.backend.data_object.random_slot.RandomSlotsData
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionNoLinks
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.BarrelItem
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

object EndEnterHotbarSortedBarrel {
    private val fixedSlotsData get() = FixedSlotsData(
        OffhandSlotData(
            optionList(
                item("bread", 1, 3, label = "bread"),
                item("cooked_porkchop", 1, 1, label = "pork"),
                item("rotten_flesh", 1, 4, label = "flesh"),
                item("cooked_salmon", 1, 1, label = "salmon"),
                emptyItem(1, label = "nofood")
            )
        ),
        listOf(
            hotbarSlot(
                0, optionList(
                    item("white_bed", 1)
                )
            ),
            hotbarSlot(
                1, optionList(
                    item("iron_pickaxe", 8),
                    item("golden_pickaxe", 2),
                    item("diamond_pickaxe", 1),
                )
            ),
            hotbarSlot(
                2, optionList(
                    item("iron_shovel", 1, label = "0a"),
                    item("stone_shovel", 1, label = "0a"),
                    item("respawn_anchor", 2, 1, label = "1a"),
                    item("respawn_anchor", 3, 2, label = "2a"),
                    item("respawn_anchor", 2, 3, label = "3a"),
                    item("respawn_anchor", 1, 4, label = "4a"),
                )
            ),
            hotbarSlot(
                3, optionList(
                    item("oak_boat", 12, conditions = condition("0a")),
                    item("birch_boat", 4, conditions = condition("0a")),
                    item("acacia_boat", 6, conditions = condition("0a")),
                    item("dark_oak_boat", 2, conditions = condition("0a")),
                    item("jungle_boat", 1, conditions = condition("0a")),
                    item("spruce_boat", 2, conditions = condition("0a")),
                    item("glowstone", 3, 1, conditions = condition("1a")),
                    item("glowstone", 1, 2, conditions = condition("1a")),
                    item("glowstone", 3, 2, conditions = condition("2a")),
                    item("glowstone", 1, 3, conditions = condition("2a")),
                    item("glowstone", 3, 3, conditions = condition("3a")),
                    item("glowstone", 1, 4, conditions = condition("3a")),
                    item("glowstone", 3, 4, conditions = condition("4a")),
                    item("glowstone", 1, 5, conditions = condition("4a")),
                )
            ),
            hotbarSlot(
                4, optionList(
                    item("nether_bricks", 2, 15),
                    item("nether_bricks", 2, 18),
                    item("nether_bricks", 3, 23),
                    item("nether_bricks", 3, 26),
                    item("nether_bricks", 3, 31),
                    item("nether_bricks", 2, 37),
                    item("nether_bricks", 1, 42),
                    item("nether_bricks", 1, 55),
                )
            ),
            hotbarSlot(
                5, optionList(
                    item("crying_obsidian", 1, 2),
                    item("crying_obsidian", 1, 4),
                    item("crying_obsidian", 1, 7),
                    item("crying_obsidian", 1, 9),
                )
            ),
            hotbarSlot(
                6, optionList(
                    item("ender_eye"),
                    emptyItem()
                )
            ),
            hotbarSlot(
                7, optionList(
                    item("ender_pearl", amount = 13)
                )
            ),
            hotbarSlot(
                8, optionList(
                    item("soul_sand", 1, 64, label="hotbar_ss"),
                    item("soul_sand", 1, 53, label="hotbar_ss"),
                    item("soul_sand", 1, 27, label="hotbar_ss"),
                    item("gravel", 1, 64, label="hotbar_gravel"),
                    item("gravel", 1, 45, label="hotbar_gravel"),
                    item("gravel", 1, 24, label="hotbar_gravel"),
                    item("netherrack", 1, 64, label="hotbar_nrack"),
                )
            ),
        ),
        listOf(
            invSlot(0, optionList(item("fire_charge", amount = 23))),
            invSlot(1, optionList(availableItem())),
            invSlot(2, optionList(availableItem())),
            invSlot(3, optionList(availableItem())),
            invSlot(4, optionList(availableItem())),
            invSlot(5, optionList(availableItem())),
            invSlot(6, optionList(availableItem())),
            invSlot(7, optionList(item("ender_pearl", amount = 4))),
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
        HelmetSlotData(
            optionList(
                item("golden_helmet", 6),
                item("iron_helmet", 1),
                emptyItem(8)
            )
        ),
        ChestplateSlotData(
            optionList(
                item("golden_chestplate", 6),
                item("iron_chestplate", 1),
                emptyItem(8)
            )
        ),
        LeggingsSlotData(
            optionList(
                item("golden_leggings", 6),
                item("iron_leggings", 1),
                emptyItem(8)
            )
        ),
        BootsSlotData(
            optionList(
                item("golden_boots", 5),
                item("iron_boots", 1),
                emptyItem(12),
                WeightedOption(EnchantedBootsItem(true, 1), 5),
                WeightedOption(EnchantedBootsItem(true, 2), 5),
                WeightedOption(EnchantedBootsItem(true, 3), 5),
                WeightedOption(EnchantedBootsItem(false, 1), 5),
                WeightedOption(EnchantedBootsItem(false, 2), 5),
                WeightedOption(EnchantedBootsItem(false, 3), 5),
            )
        ),
    )

    private val randomSlotsData get() = RandomSlotsData(true,
        listOf(
            RandomSlotOptionsSet(
                "Inventory Beds",
                WeightedOptionList(mutableListOf(
                    itemList(rawItem("white_bed", 4), weight = 1, conditions = condition("0a")),
                    itemList(rawItem("white_bed", 5), weight = 3, conditions = condition("0a")),
                    itemList(rawItem("white_bed", 6), weight = 2, conditions = condition("0a")),
                    itemList(rawItem("white_bed", 7), weight = 1, conditions = condition("0a")),

                    itemList(rawItem("white_bed", 3), weight = 2, conditions = condition("1a")),
                    itemList(rawItem("white_bed", 4), weight = 8, conditions = condition("1a")),
                    itemList(rawItem("white_bed", 5), weight = 6, conditions = condition("1a")),
                    itemList(rawItem("white_bed", 6), weight = 2, conditions = condition("1a")),

                    itemList(rawItem("white_bed", 2), weight = 1, conditions = condition("2a")),
                    itemList(rawItem("white_bed", 3), weight = 3, conditions = condition("2a")),
                    itemList(rawItem("white_bed", 4), weight = 2, conditions = condition("2a")),
                    itemList(rawItem("white_bed", 5), weight = 1, conditions = condition("2a")),

                    itemList(rawItem("white_bed", 1), weight = 2, conditions = condition("3a")),
                    itemList(rawItem("white_bed", 2), weight = 4, conditions = condition("3a")),
                    itemList(rawItem("white_bed", 3), weight = 3, conditions = condition("3a")),
                    itemList(rawItem("white_bed", 4), weight = 2, conditions = condition("3a")),

                    itemList(availableItem().option, weight = 1, conditions = condition("4a")),
                    itemList(rawItem("white_bed", 1), weight = 3, conditions = condition("4a")),
                    itemList(rawItem("white_bed", 2), weight = 2, conditions = condition("4a")),
                    itemList(rawItem("white_bed", 3), weight = 1, conditions = condition("4a")),
                ))
            ),
            RandomSlotOptionsSet(
                "Bow",
                WeightedOptionList(mutableListOf(
                    itemList(rawItem("bow"), weight = 3),
                    itemList(rawItem("crossbow"), weight = 2),
                    itemList(availableItem().option, weight = 1)
                ))
            ),
            RandomSlotOptionsSet(
                "Arrows",
                WeightedOptionList(mutableListOf(
                    itemList(rawItem("arrow", 6), weight = 3),
                    itemList(rawItem("arrow", 12), weight = 2),
                    itemList(rawItem("arrow", 20), rawItem("spectral_arrow", 13), weight = 2),
                    itemList(rawItem("arrow", 34), weight = 1),
                    itemList(rawItem("arrow", 50), weight = 1),
                    itemList(rawItem("arrow", 64), weight = 2),
                    itemList(rawItem("arrow", 64), rawItem("spectral_arrow", 30), weight = 1),
                    itemList(availableItem().option, weight = 3)
                ))
            ),
            RandomSlotOptionsSet(
                "Spare Building Blocks",
                WeightedOptionList(mutableListOf(
                    itemList(
                        rawItem("gravel", 110),
                        rawItem("soul_sand", 64),
                        weight = 1,
                        conditions = condition("hotbar_nrack")
                    ),
                    itemList(
                        rawItem("gravel", 64),
                        rawItem("soul_sand", 115),
                        weight = 1,
                        conditions = condition("hotbar_nrack")
                    ),
                    itemList(
                        rawItem("soul_sand", 120),
                        rawItem("netherrack", 64),
                        weight = 1,
                        conditions = condition("hotbar_gravel")
                    ),
                    itemList(
                        rawItem("soul_sand", 78),
                        weight = 3,
                        conditions = condition("hotbar_gravel")
                    ),
                    itemList(
                        rawItem("gravel", 93),
                        rawItem("netherrack", 64),
                        weight = 1,
                        conditions = condition("hotbar_ss")
                    ),
                    itemList(
                        rawItem("gravel", 93),
                        weight = 3,
                        conditions = condition("hotbar_ss")
                    ),
                ))
            ),
            RandomSlotOptionsSet(
                "Axe",
                WeightedOptionList(mutableListOf(
                    itemList(rawItem("iron_axe", 2), weight = 1),
                    itemList(rawItem("stone_axe", 1), weight = 1),
                ))
            ),
            RandomSlotOptionsSet(
                "Sticks",
                WeightedOptionList(mutableListOf(
                    itemList(rawItem("stick", 1), weight = 1),
                    itemList(rawItem("stick", 2), weight = 1),
                    itemList(availableItem().option, weight = 1)
                ))
            ),
            RandomSlotOptionsSet(
                "Leftover Obby",
                WeightedOptionList(mutableListOf(
                    itemList(rawItem("obsidian", 5), weight = 1),
                    itemList(rawItem("obsidian", 1), weight = 1),
                    itemList(availableItem().option, weight = 1)
                ))
            ),
            RandomSlotOptionsSet(
                "Wood",
                WeightedOptionList(mutableListOf(
                    itemList(rawItem("oak_planks", 5), weight = 7),
                    itemList(rawItem("acacia_planks", 5), weight = 2),
                    itemList(rawItem("birch_planks", 5), weight = 1),
                    itemList(availableItem().option, weight = 1)
                ))
            ),
            RandomSlotOptionsSet(
                "Bucket",
                WeightedOptionList(mutableListOf(
                    itemList(rawItem("lava_bucket"), weight = 6),
                    itemList(rawItem("water_bucket"), weight = 1),
                    itemList(availableItem().option, weight = 2)
                ))
            ),
            RandomSlotOptionsSet(
                "Boat",
                WeightedOptionList(mutableListOf(
                    itemList(rawItem("oak_boat"), weight = 12, conditions = invCondition("0a")),
                    itemList(rawItem("birch_boat"), weight = 4, conditions = invCondition("0a")),
                    itemList(rawItem("acacia_boat"), weight = 6, conditions = invCondition("0a")),
                    itemList(rawItem("dark_oak_boat"), weight = 2, conditions = invCondition("0a")),
                    itemList(rawItem("jungle_boat"), weight = 1, conditions = invCondition("0a")),
                    itemList(rawItem("spruce_boat"), weight = 2, conditions = invCondition("0a")),
                    itemList(availableItem().option, conditions = condition("0a"))
                ))
            ),
            RandomSlotOptionsSet(
                "Shovel",
                WeightedOptionList(mutableListOf(
                    itemList(rawItem("iron_shovel"), weight = 1, conditions = invCondition("0a")),
                    itemList(rawItem("stone_shovel"), weight = 1, conditions = invCondition("0a")),
                    itemList(availableItem().option, conditions = condition("0a"))
                ))
            ),
            RandomSlotOptionsSet(
                "Force Perch Potion",
                WeightedOptionList(mutableListOf(
                    itemList(ForcePerchPotionItem()),
                ))
            ),
        )
    )

    private val junkSettings get() = JunkSettings(true, true, listOf(
        WeightedOptionNoLinks(SplashFireResItem(), 2),
        WeightedOptionNoLinks(FireResItem(), 2),
        WeightedOptionNoLinks(rawItem("string", 2)),
        WeightedOptionNoLinks(rawItem("iron_ingot", 4)),
        WeightedOptionNoLinks(rawItem("iron_nugget", 40)),
        WeightedOptionNoLinks(rawItem("gold_ingot", 2)),
        WeightedOptionNoLinks(rawItem("golden_boots", 1)),
        WeightedOptionNoLinks(rawItem("leather", 64), 2),
        WeightedOptionNoLinks(rawItem("glowstone_dust", 3)),
        WeightedOptionNoLinks(rawItem("cracked_stone_bricks", 1)),
        WeightedOptionNoLinks(rawItem("mossy_stone_bricks", 1)),
        WeightedOptionNoLinks(rawItem("tnt", 1)),
        WeightedOptionNoLinks(rawItem("nether_brick", 3)),
        WeightedOptionNoLinks(rawItem("dirt", 4)),
        WeightedOptionNoLinks(rawItem("nether_brick_fence", 1)),
        WeightedOptionNoLinks(EnchantedBootsItem(true, 1)),
        WeightedOptionNoLinks(SoulSpeedBookItem(1)),
        WeightedOptionNoLinks(rawItem("polished_blackstone_bricks", 3), 1),
        WeightedOptionNoLinks(rawItem("basalt", 2), 1),
    ))

    private val healthHungerSettings get() = HealthHungerSettings(WeightedOptionList(mutableListOf(
        WeightedOption(HealthHungerOption(HealthOption.FULL_HEALTH, HungerOption.HUNGER_RESET), 5, _conditions= condition("nofood")),
        WeightedOption(HealthHungerOption(HealthOption.FULL_HEALTH, HungerOption.GOLDEN_CARROT), 1, _conditions= condition("nofood")),
        WeightedOption(HealthHungerOption(HealthOption.FULL_HEALTH, HungerOption.ROTTEN_FLESH), 1, _conditions= condition("flesh")),
        WeightedOption(HealthHungerOption(HealthOption.FULL_HEALTH, HungerOption.BREAD), 1, _conditions= condition("bread")),
        WeightedOption(HealthHungerOption(HealthOption.FULL_HEALTH, HungerOption.BEEF), 1, _conditions= condition("pork")),
    )))

    private val fireResSettings get() = FireResSettings(WeightedOptionList(mutableListOf(
        WeightedOption(0, 2),
        WeightedOption(30, 1),
        WeightedOption(60, 1),
        WeightedOption(90, 1),
    )))

    private val allRandomiserLinkLabels = hashSetOf("nofood", "flesh", "pork", "bread", "salmon", "0a", "1a", "2a", "3a", "4a", "hotbar_ss", "hotbar_gravel", "hotbar_nrack")

    val barrel get() = BarrelItem("End Enter - Hotbar Sorted", PracticeTypeOption.END_ENTER, GamemodeOption.SURVIVAL, DifficultyOption.EASY, fixedSlotsData, randomSlotsData, junkSettings, healthHungerSettings, fireResSettings, allRandomiserLinkLabels)
}