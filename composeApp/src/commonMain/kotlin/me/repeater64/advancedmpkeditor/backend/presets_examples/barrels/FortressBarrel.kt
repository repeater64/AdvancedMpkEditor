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
import me.repeater64.advancedmpkeditor.backend.data_object.item.DontReplaceMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.EnchantedBootsItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.FireResItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.ForcePerchPotionItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.SoulSpeedBookItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.SplashFireResItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.SurfaceBlindPotionItem
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
import me.repeater64.advancedmpkeditor.backend.presets_examples.invSlot
import me.repeater64.advancedmpkeditor.backend.presets_examples.item
import me.repeater64.advancedmpkeditor.backend.presets_examples.itemList
import me.repeater64.advancedmpkeditor.backend.presets_examples.optionList
import me.repeater64.advancedmpkeditor.backend.presets_examples.rawItem

// TODO set up fortress barrel
object FortressBarrel {
    private val fixedSlotsData get() = FixedSlotsData(
        OffhandSlotData(
            optionList(
                item("bread", 1, 10, label = "bread"),
                item("cooked_porkchop", 1, 4, label = "pork"),
                item("rotten_flesh", 1, 15, label = "flesh"),
                item("golden_carrot", 1, 4, label = "gcarrot"),
                item("cooked_salmon", 1, 3, label = "salmon"),
            )
        ),
        listOf(
            hotbarSlot(
                0, optionList(
                    item("iron_axe", 2),
                    item("stone_axe", 1),
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
                    item("iron_shovel", 1),
                    item("stone_shovel", 1),
                )
            ),
            hotbarSlot(
                3, optionList(
                    item("oak_boat", 12),
                    item("birch_boat", 4),
                    item("acacia_boat", 6),
                    item("dark_oak_boat", 2),
                    item("jungle_boat", 1),
                    item("spruce_boat", 2),
                )
            ),
            hotbarSlot(
                4, optionList(
                    item("soul_sand", 1, 55),
                    item("soul_sand", 1, 47),
                    item("soul_sand", 1, 33),
                    item("soul_sand", 1, 28),
                    item("soul_sand", 1, 19),
                )
            ),
            hotbarSlot(
                5, optionList(
                    item("gravel", 1, 50),
                    item("gravel", 1, 44),
                    item("gravel", 1, 38),
                    item("gravel", 1, 27),
                )
            ),
            hotbarSlot(
                6, optionList(
                    item("fire_charge", 1, 23),
                    item("fire_charge", 1, 17)
                )
            ),
            hotbarSlot(
                7, optionList(
                    item("ender_pearl", amount = 16)
                )
            ),
            hotbarSlot(
                8, optionList(
                    item("ender_eye", 1, 2),
                    item("ender_eye", 1, 4),
                    item("ender_eye", 1, 6),
                    item("ender_eye", 1, 8),
                    item("ender_eye", 1, 10),
                    item("ender_eye", 1, 12),
                    item("ender_eye", 1, 14),
                )
            ),
        ),
        listOf(
            invSlot(0, optionList(item("obsidian", amount = 10), item("obsidian", amount = 11), item("obsidian", amount = 14))),
            invSlot(1, optionList(availableItem())),
            invSlot(2, optionList(availableItem())),
            invSlot(3, optionList(availableItem())),
            invSlot(4, optionList(availableItem())),
            invSlot(5, optionList(availableItem())),
            invSlot(6, optionList(item("ender_pearl", amount = 5))),
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

    private val randomSlotsData get() = RandomSlotsData(
        listOf(
            RandomSlotOptionsSet(
                "Explosive Ingredients",
                WeightedOptionList(mutableListOf(
                    itemList(rawItem("string", 5*12+2), rawItem("crying_obsidian", 6), rawItem("glowstone_dust", 15), weight = 1),
                    itemList(rawItem("string", 4*12+3), rawItem("crying_obsidian", 1*6+3), rawItem("glowstone_dust", 1*16+14), weight = 1),
                    itemList(rawItem("string", 3*12+4), rawItem("crying_obsidian", 2*6+9), rawItem("glowstone_dust", 2*16+10), weight = 1),
                    itemList(rawItem("string", 2*12+5), rawItem("crying_obsidian", 3*6+5), rawItem("glowstone_dust", 3*16+3), weight = 1),

                    itemList(rawItem("string", 6*12+7), rawItem("crying_obsidian", 4), rawItem("glowstone_dust", 11), weight = 3),
                    itemList(rawItem("string", 5*12+4), rawItem("crying_obsidian", 1*6+20), rawItem("glowstone_dust", 1*16+14), weight = 3),
                    itemList(rawItem("string", 4*12+5), rawItem("crying_obsidian", 2*6+1), rawItem("glowstone_dust", 2*16+2), weight = 3),
                    itemList(rawItem("string", 3*12+6), rawItem("crying_obsidian", 3*6+7), rawItem("glowstone_dust", 3*16+8), weight = 3),

                    itemList(rawItem("string", 7*12+10), rawItem("crying_obsidian", 10), rawItem("glowstone_dust", 6), weight = 2),
                    itemList(rawItem("string", 6*12+9), rawItem("crying_obsidian", 1*6+4), rawItem("glowstone_dust", 1*16+3), weight = 2),
                    itemList(rawItem("string", 5*12+6), rawItem("crying_obsidian", 2*6+16), rawItem("glowstone_dust", 2*16+7), weight = 2),
                    itemList(rawItem("string", 4*12+7), rawItem("crying_obsidian", 3*6+3), rawItem("glowstone_dust", 3*16+12), weight = 2),

                    itemList(rawItem("string", 8*12+10), rawItem("crying_obsidian", 5), rawItem("glowstone_dust", 9), weight = 1),
                    itemList(rawItem("string", 7*12+5), rawItem("crying_obsidian", 1*6+20), rawItem("glowstone_dust", 1*16+5), weight = 1),
                    itemList(rawItem("string", 6*12+11), rawItem("crying_obsidian", 2*6), rawItem("glowstone_dust", 2*16+1), weight = 1),
                    itemList(rawItem("string", 5*12+8), rawItem("crying_obsidian", 3*6), rawItem("glowstone_dust", 3*16+13), weight = 1),
                ))
            ),
            RandomSlotOptionsSet(
                "Potential Crossbow",
                WeightedOptionList(mutableListOf(
                    itemList(rawItem("crossbow"), weight = 1),
                    itemList(availableItem().option, weight = 3)
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
                "Other Blocks",
                WeightedOptionList(mutableListOf(
                    itemList(
                        rawItem("gravel", 45),
                        rawItem("oak_planks", 31),
                        rawItem("soul_sand", 64),
                        weight = 5
                    ),
                    itemList(
                        rawItem("oak_planks", 50),
                        rawItem("soul_sand", 64),
                        weight = 2
                    ),
                    itemList(
                        rawItem("gravel", 36),
                        rawItem("oak_planks", 26),
                        weight = 1
                    ),
                    itemList(
                        rawItem("gravel", 120),
                        rawItem("oak_planks", 29),
                        weight = 1
                    ),
                    itemList(
                        rawItem("oak_planks", 25),
                        rawItem("netherrack", 53),
                        weight = 3
                    ),
                    itemList(
                        rawItem("oak_planks", 35),
                        rawItem("netherrack", 94),
                        weight = 1
                    ),
                ))
            ),
            RandomSlotOptionsSet(
                "Sticks",
                WeightedOptionList(mutableListOf(
                    itemList(rawItem("stick", 1), weight = 1),
                    itemList(rawItem("stick", 2), weight = 1),
                    itemList(rawItem("stick", 3), weight = 1),
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
                "Nether Bricks",
                WeightedOptionList(mutableListOf(
                    itemList(rawItem("nether_bricks", 15), weight=2),
                    itemList(rawItem("nether_bricks", 18), weight=2),
                    itemList(rawItem("nether_bricks", 23), weight=3),
                    itemList(rawItem("nether_bricks", 26), weight=3),
                    itemList(rawItem("nether_bricks", 31), weight=3),
                    itemList(rawItem("nether_bricks", 37), weight=2),
                    itemList(rawItem("nether_bricks", 42), weight=1),
                    itemList(rawItem("nether_bricks", 55), weight=1),
                ))
            ),
            RandomSlotOptionsSet(
                "Leftover Gold",
                WeightedOptionList(mutableListOf(
                    itemList(availableItem().option, weight = 1),
                    itemList(rawItem("gold_ingot", 5), weight = 1),
                    itemList(rawItem("gold_ingot", 8), weight = 1),
                    itemList(rawItem("gold_ingot", 12), weight = 1),
                ))
            ),
            RandomSlotOptionsSet(
                "Fire Res",
                WeightedOptionList(mutableListOf(
                    itemList(FireResItem(), weight = 1),
                    itemList(SplashFireResItem(), weight = 1),
                    itemList(SplashFireResItem(), FireResItem(), weight = 1),
                    itemList(SplashFireResItem(), SplashFireResItem(), weight = 1),
                    itemList(FireResItem(), FireResItem(), weight = 1),
                ))
            ),
            RandomSlotOptionsSet(
                "Crafting Table",
                WeightedOptionList(mutableListOf(
                    itemList(rawItem("crafting_table")),
                ))
            ),
        )
    )

    private val junkSettings get() = JunkSettings(false, true, listOf(
        WeightedOptionNoLinks(DontReplaceMinecraftItem()),
    ))

    private val healthHungerSettings get() = HealthHungerSettings(WeightedOptionList(mutableListOf(
        WeightedOption(HealthHungerOption(HealthOption.DOWN_3_HEARTS, HungerOption.ROTTEN_FLESH), 1, _conditions= condition("flesh")),
        WeightedOption(HealthHungerOption(HealthOption.DOWN_6_HEARTS, HungerOption.ROTTEN_FLESH), 1, _conditions= condition("flesh")),
        WeightedOption(HealthHungerOption(HealthOption.DOWN_3_HEARTS, HungerOption.BREAD), 1, _conditions= condition("bread")),
        WeightedOption(HealthHungerOption(HealthOption.DOWN_6_HEARTS, HungerOption.BREAD), 1, _conditions= condition("bread")),
        WeightedOption(HealthHungerOption(HealthOption.DOWN_3_HEARTS, HungerOption.BEEF), 1, _conditions= condition("pork")),
        WeightedOption(HealthHungerOption(HealthOption.DOWN_6_HEARTS, HungerOption.BEEF), 1, _conditions= condition("pork")),
        WeightedOption(HealthHungerOption(HealthOption.DOWN_3_HEARTS, HungerOption.MUTTON), 1, _conditions= condition("salmon")),
        WeightedOption(HealthHungerOption(HealthOption.DOWN_6_HEARTS, HungerOption.MUTTON), 1, _conditions= condition("salmon")),
        WeightedOption(HealthHungerOption(HealthOption.DOWN_3_HEARTS, HungerOption.GOLDEN_CARROT), 1, _conditions= condition("gcarrot")),
        WeightedOption(HealthHungerOption(HealthOption.DOWN_6_HEARTS, HungerOption.GOLDEN_CARROT), 1, _conditions= condition("gcarrot")),
    )))

    private val fireResSettings get() = FireResSettings(WeightedOptionList(mutableListOf(
        WeightedOption(0, 1),
    )))

    private val allRandomiserLinkLabels = hashSetOf("gcarrot", "flesh", "pork", "bread", "salmon")

    val barrel get() = BarrelItem("Fortress", PracticeTypeOption.FORTRESS, GamemodeOption.SURVIVAL, DifficultyOption.EASY, fixedSlotsData, randomSlotsData, junkSettings, healthHungerSettings, fireResSettings, allRandomiserLinkLabels)
}