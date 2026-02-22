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
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.RandomiserCondition
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

object BastionBarrel {
    private val fixedSlotsData get() = FixedSlotsData(
        OffhandSlotData(
            optionList(
                item("bread", 6, 20, conditions = listOf(RandomiserCondition("desert_temple", true), RandomiserCondition("ocean", true))),
                item("cooked_porkchop", 3, 6, conditions = listOf(RandomiserCondition("desert_temple", true), RandomiserCondition("ocean", true))),
                item("cooked_mutton", 2, 7, conditions = listOf(RandomiserCondition("desert_temple", true), RandomiserCondition("ocean", true))),
                item("golden_carrot", 1, 8, conditions = listOf(RandomiserCondition("desert_temple", true), RandomiserCondition("ocean", true))),
                item("cooked_salmon", 1, 4, conditions=condition("ocean")),
                item("rotten_flesh", 1, 35, label = "desert_temple"),
            )
        ),
        listOf(
            hotbarSlot(
                0, optionList(
                    item("iron_axe", 1),
                    item("golden_axe", 1),
                    item("stone_axe", 1),
                )
            ),
            hotbarSlot(
                1, optionList(
                    item("iron_pickaxe", 8),
                    item("diamond_pickaxe", 1, conditions = condition("lavabucket")),
                )
            ),
            hotbarSlot(
                2, optionList(
                    item("iron_shovel", 4),
                    item("stone_shovel", 3),
                    item("golden_shovel", 2),
                    item("diamond_shovel", 1),
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
                    item("oak_planks", 3, 50, label="hotbar_planks"),
                    item("acacia_planks", 1, 50, label="hotbar_planks"),
                    item("oak_leaves", 2, 64, label="ocean"),
                    item("birch_leaves", 1, 64, label="ocean"),
                    item("dirt", 1, 64, label="ocean"),
                    item("dirt", 1, 64, label="desert_temple"),
                    item("cut_sandstone", 1, 64, label="desert_temple"),
                )
            ),
            hotbarSlot(
                5, optionList(
                    item("flint_and_steel", 5),
                    emptyItem(1),
                )
            ),
            hotbarSlot(
                6, optionList(
                    item("crafting_table", 1, 1)
                )
            ),
            hotbarSlot(
                7, optionList(
                    item("magma_block", amount = 2, conditions = condition("ocean")),
                    item("oak_door", amount = 1, conditions = condition("ocean")),
                    item("tnt", amount = 5, conditions = condition("desert_temple")),
                    emptyItem(conditions = invCondition("desert_temple"))
                )
            ),
            hotbarSlot(
                8, optionList(
                    item("lava_bucket", 2, label="lavabucket"),
                    emptyItem(1, conditions = listOf(RandomiserCondition("desert_temple", true), RandomiserCondition("ocean", true))),
                )
            ),
        ),
        listOf(
            invSlot(0, optionList(
                item("oak_planks", amount = 37, weight = 4, conditions = invCondition("hotbar_planks")),
                item("acacia_planks", amount = 40, weight = 1, conditions = invCondition("hotbar_planks")),
                availableItem(conditions=condition("hotbar_planks"))
            )),
            invSlot(1, optionList(item("stick", 2))),
            invSlot(2, optionList(availableItem())),
            invSlot(3, optionList(availableItem())),
            invSlot(4, optionList(item("cooked_cod", 1, 2, conditions=condition("ocean")), availableItem(conditions=invCondition("ocean")))),
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
        HelmetSlotData(
            optionList(
                item("golden_helmet", 1, conditions = invCondition("lavabucket")),
                emptyItem(2)
            )
        ),
        ChestplateSlotData(
            optionList(
                item("golden_chestplate", 1, conditions = invCondition("lavabucket")),
                emptyItem(2)
            )
        ),
        LeggingsSlotData(
            optionList(
                item("golden_leggings", 1, conditions = invCondition("lavabucket")),
                emptyItem(2)
            )
        ),
        BootsSlotData(
            optionList(
                item("golden_boots", 1, conditions = invCondition("lavabucket")),
                emptyItem(2),
            )
        ),
    )

    private val randomSlotsData get() = RandomSlotsData(false, emptyList())

    private val junkSettings get() = JunkSettings(false, true, listOf(
        WeightedOptionNoLinks(DontReplaceMinecraftItem()),
    ))

    private val healthHungerSettings get() = HealthHungerSettings(WeightedOptionList(mutableListOf(
        WeightedOption(HealthHungerOption(HealthOption.DOWN_3_HEARTS, HungerOption.ROTTEN_FLESH), 1, _conditions= condition("desert_temple")),
        WeightedOption(HealthHungerOption(HealthOption.FULL_HEALTH, HungerOption.FULL_HUNGER_NO_SAT), 1, _conditions= invCondition("desert_temple")),
    )))

    private val fireResSettings get() = FireResSettings(WeightedOptionList(mutableListOf(
        WeightedOption(0, 1),
    )))

    private val allRandomiserLinkLabels = hashSetOf("ocean", "desert_temple", "hotbar_planks", "lavabucket")

    val barrel get() = BarrelItem("Bastion", PracticeTypeOption.BASTION, GamemodeOption.SURVIVAL, DifficultyOption.EASY, fixedSlotsData, randomSlotsData, junkSettings, healthHungerSettings, fireResSettings, allRandomiserLinkLabels)
}