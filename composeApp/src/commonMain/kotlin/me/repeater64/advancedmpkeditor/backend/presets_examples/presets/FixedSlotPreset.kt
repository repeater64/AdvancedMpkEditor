package me.repeater64.advancedmpkeditor.backend.presets_examples.presets

import me.repeater64.advancedmpkeditor.backend.data_object.item.EnchantedBootsItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.LootingSwordItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItemCategory
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.backend.presets_examples.emptyItem
import me.repeater64.advancedmpkeditor.backend.presets_examples.item
import me.repeater64.advancedmpkeditor.backend.presets_examples.optionList

enum class FixedSlotPreset(
    val displayName: String,
    val optionsGetter: () -> WeightedOptionList<MinecraftItem>,
    val onlyIfOneCategory: MinecraftItemCategory? = null
) {
    RANDOM_BOAT( "Random Boat", {
        optionList(
            item("oak_boat", 12),
            item("birch_boat", 4),
            item("acacia_boat", 6),
            item("dark_oak_boat", 2),
            item("jungle_boat", 1),
            item("spruce_boat", 2),
        )
    }),

    RANDOM_PICKAXE( "Random Pickaxe", {
        optionList(
            item("iron_pickaxe", 8),
            item("golden_pickaxe", 3),
            item("diamond_pickaxe", 1),
        )
    }),

    RANDOM_SHOVEL( "Random Shovel", {
        optionList(
            item("iron_shovel", 3),
            item("stone_shovel", 3),
            item("golden_shovel", 2),
            item("diamond_shovel", 1),
        )
    }),

    RANDOM_AXE( "Random Axe", {
        optionList(
            item("iron_axe", 3),
            item("stone_axe", 3),
            item("golden_axe", 2),
            item("diamond_axe", 1),
        )
    }),

    RANDOM_SWORD( "Random Sword", {
        optionList(
            item("iron_sword", 20),
            item("stone_sword", 20),
            item("diamond_sword", 5),
            WeightedOption(LootingSwordItem(1), 1),
            WeightedOption(LootingSwordItem(2), 1),
            WeightedOption(LootingSwordItem(3), 1),
        )
    }),

    RANDOM_HELMET("Random Helmet", onlyIfOneCategory = MinecraftItemCategory.ALL_HELMETS, optionsGetter = {
        optionList(
            item("golden_helmet", 6),
            item("iron_helmet", 1),
            emptyItem(8)
        )
    }),

    RANDOM_CHESTPLATE("Random Chestplate", onlyIfOneCategory = MinecraftItemCategory.ALL_CHESTPLATES, optionsGetter = {
        optionList(
            item("golden_chestplate", 6),
            item("iron_chestplate", 1),
            emptyItem(8)
        )
    }),

    RANDOM_LEGGINGS("Random Leggings", onlyIfOneCategory = MinecraftItemCategory.ALL_LEGGINGS, optionsGetter = {
        optionList(
            item("golden_leggings", 6),
            item("iron_leggings", 1),
            emptyItem(8)
        )
    }),

    RANDOM_BOOTS("Random Boots", onlyIfOneCategory = MinecraftItemCategory.ALL_BOOTS, optionsGetter = {
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
    }),

    ;


    val options by lazy {optionsGetter()} // Can use this to render the icons, but re-call optionsGetter each time we use the preset so we don't end up mutating the objects when edited
}