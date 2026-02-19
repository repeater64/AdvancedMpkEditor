package me.repeater64.advancedmpkeditor.backend.presets_examples

import me.repeater64.advancedmpkeditor.backend.data_object.item.EnchantedBootsItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.FireResItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.LootingSwordItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItemCategory
import me.repeater64.advancedmpkeditor.backend.data_object.item.SoulSpeedBookItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.SplashFireResItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionNoLinks

enum class JunkPreset(
    val displayName: String,
    val optionsGetter: () -> List<WeightedOptionNoLinks<MinecraftItem>>,
) {
    BARTER_JUNK("Barter Junk", {
        listOf(
            WeightedOptionNoLinks(rawItem("leather", 31), 1),
            WeightedOptionNoLinks(rawItem("magma_cream", 17), 1),
            WeightedOptionNoLinks(rawItem("quartz", 40), 1),
            WeightedOptionNoLinks(rawItem("iron_nugget", 64), 1),
            WeightedOptionNoLinks(SoulSpeedBookItem(1), 1),
            WeightedOptionNoLinks(FireResItem(), 2),
            WeightedOptionNoLinks(SplashFireResItem(), 1),
            WeightedOptionNoLinks(EnchantedBootsItem(true, 2), 1),
        )
    }),

    BASTION_BLOCKS("Bastion Blocks", {
        listOf(
            WeightedOptionNoLinks(rawItem("polished_blackstone_bricks", 3), 1),
            WeightedOptionNoLinks(rawItem("basalt", 2), 1),
            WeightedOptionNoLinks(rawItem("blackstone", 5), 1),
            WeightedOptionNoLinks(rawItem("cracked_polished_blackstone_bricks", 4), 1),
        )
    }),

    MISC_END_ENTER_JUNK("Misc End Enter Junk", {
        listOf(
            WeightedOptionNoLinks(rawItem("cracked_stone_bricks", 1), 1),
            WeightedOptionNoLinks(rawItem("mossy_stone_bricks", 1), 1),
            WeightedOptionNoLinks(rawItem("nether_brick_fence", 3), 1),
            WeightedOptionNoLinks(rawItem("bone", 2), 1),
        )
    }),

    DT_JUNK("Desert Temple Junk", {
        listOf(
            WeightedOptionNoLinks(rawItem("sand", 14), 1),
            WeightedOptionNoLinks(rawItem("chest", 4), 1),
            WeightedOptionNoLinks(rawItem("bone", 19), 1),
            WeightedOptionNoLinks(rawItem("spider_eye", 6), 1),
            WeightedOptionNoLinks(rawItem("chiseled_sandstone", 15), 1),
            WeightedOptionNoLinks(rawItem("sandstone", 21), 1),
            WeightedOptionNoLinks(rawItem("saddle", 1), 2),
            WeightedOptionNoLinks(rawItem("iron_horse_armor", 1), 1),
            WeightedOptionNoLinks(rawItem("golden_horse_armor", 1), 1),
            WeightedOptionNoLinks(rawItem("cobblestone", 11), 1),
            WeightedOptionNoLinks(rawItem("gunpowder", 24), 1),
            WeightedOptionNoLinks(rawItem("stone_pressure_plate", 1), 1),
        )
    }),

    ;


    val options by lazy {optionsGetter()} // Can use this to render the icons, but re-call optionsGetter each time we use the preset so we don't end up mutating the objects when edited
}