package me.repeater64.advancedmpkeditor.backend.presets_examples.presets

import androidx.compose.runtime.snapshots.SnapshotStateList
import me.repeater64.advancedmpkeditor.backend.data_object.item.LootingSwordItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.backend.presets_examples.availableItem
import me.repeater64.advancedmpkeditor.backend.presets_examples.explosiveIngredients
import me.repeater64.advancedmpkeditor.backend.presets_examples.explosives
import me.repeater64.advancedmpkeditor.backend.presets_examples.item
import me.repeater64.advancedmpkeditor.backend.presets_examples.itemList
import me.repeater64.advancedmpkeditor.backend.presets_examples.optionList
import me.repeater64.advancedmpkeditor.backend.presets_examples.randomString
import me.repeater64.advancedmpkeditor.backend.presets_examples.rawItem

enum class RandomSlotPreset(
    val displayName: String,
    val iconItem: MinecraftItem,
    val optionsGetter: () -> WeightedOptionList<SnapshotStateList<MinecraftItem>>,
) {
    RANDOM_BOAT( "Random Boat", rawItem("oak_boat"), {
        WeightedOptionList(mutableListOf(
            itemList(rawItem("oak_boat"), weight = 12),
            itemList(rawItem("birch_boat"), weight = 4),
            itemList(rawItem("acacia_boat"), weight = 6),
            itemList(rawItem("dark_oak_boat"), weight = 2),
            itemList(rawItem("jungle_boat"), weight = 1),
            itemList(rawItem("spruce_boat"), weight = 2),
        ))
    }),

    RANDOM_PICKAXE( "Random Pickaxe", rawItem("iron_pickaxe"), {
        WeightedOptionList(mutableListOf(
            itemList(rawItem("iron_pickaxe"), weight = 8),
            itemList(rawItem("golden_pickaxe"), weight = 3),
            itemList(rawItem("diamond_pickaxe"), weight = 1),
        ))
    }),

    RANDOM_SHOVEL( "Random Shovel", rawItem("iron_shovel"), {
        WeightedOptionList(mutableListOf(
            itemList(rawItem("iron_shovel"), weight = 3),
            itemList(rawItem("stone_shovel"), weight = 3),
            itemList(rawItem("golden_shovel"), weight = 2),
            itemList(rawItem("diamond_shovel"), weight = 1),
        ))
    }),

    RANDOM_AXE( "Random Axe", rawItem("iron_axe"), {
        WeightedOptionList(mutableListOf(
            itemList(rawItem("iron_axe"), weight = 3),
            itemList(rawItem("stone_axe"), weight = 3),
            itemList(rawItem("golden_axe"), weight = 2),
            itemList(rawItem("diamond_axe"), weight = 1),
        ))
    }),

    RANDOM_SWORD( "Random Sword", rawItem("iron_sword"), {
        WeightedOptionList(mutableListOf(
            itemList(rawItem("iron_sword"), weight = 20),
            itemList(rawItem("stone_sword"), weight = 20),
            itemList(rawItem("diamond_sword"), weight = 5),
            itemList(LootingSwordItem(1), weight = 1),
            itemList(LootingSwordItem(2), weight = 1),
            itemList(LootingSwordItem(3), weight = 1),
        ))
    }),

    BOW_CROSSBOW_NEITHER( "Bow/Crossbow/Neither", rawItem("bow"), {
        WeightedOptionList(mutableListOf(
            itemList(rawItem("bow"), weight = 3),
            itemList(rawItem("crossbow"), weight = 2),
            itemList(availableItem().option, weight = 1)
        ))
    }),

    ARROWS( "Random Arrows (or none)", rawItem("arrow"), {
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
    }),

    BUCKET( "Random Bucket (or none)", rawItem("lava_bucket"), {
        WeightedOptionList(mutableListOf(
            itemList(rawItem("lava_bucket"), weight = 6),
            itemList(rawItem("bucket"), weight = 1),
            itemList(rawItem("water_bucket"), weight = 1),
            itemList(availableItem().option, weight = 2)
        ))
    }),

    RANDOM_FOOD("Random Food", rawItem("bread"), {
        WeightedOptionList(mutableListOf(
            itemList(rawItem("bread", 15), weight= 2),
            itemList(rawItem("cooked_porkchop", 6), weight=1),
            itemList(rawItem("cooked_mutton", 7), weight=1),
            itemList(rawItem("golden_carrot", 8), weight=1),
            itemList(rawItem("cooked_salmon", 4), weight=2),
            itemList(rawItem("rotten_flesh", 35), weight=2),
        ))
    }),

    NETHER_BRICKS("Random Amount of Nether Bricks", rawItem("nether_bricks"), {
        WeightedOptionList(mutableListOf(
            itemList(rawItem("nether_bricks", 10), weight=1),
            itemList(rawItem("nether_bricks", 18), weight=2),
            itemList(rawItem("nether_bricks", 23), weight=3),
            itemList(rawItem("nether_bricks", 26), weight=3),
            itemList(rawItem("nether_bricks", 31), weight=2),
            itemList(rawItem("nether_bricks", 37), weight=1),
            itemList(rawItem("nether_bricks", 42), weight=1),
        ))
    }),

    LEFTOVER_CRYING("Leftover Crying", rawItem("crying_obsidian"), {
        WeightedOptionList(mutableListOf(
            itemList(rawItem("crying_obsidian", 2), weight=2),
            itemList(rawItem("crying_obsidian", 5), weight=2),
            itemList(rawItem("crying_obsidian", 13), weight=2),
            itemList(rawItem("crying_obsidian", 20), weight=1),
        ))
    })

    ;


    val options by lazy {optionsGetter()} // Can use this to render the icons, but re-call optionsGetter each time we use the preset so we don't end up mutating the objects when edited
}

enum class RandomSlotPresetGroup(
    val displayName: String,
    val iconItem: MinecraftItem,
    val elements: List<RandomSlotPresetGroupElement>
) {
    BEDS("Random Amount of Beds", rawItem("white_bed"), listOf(
        RandomSlotPresetGroupElement("4 to 5 Beds") {
            WeightedOptionList(mutableListOf(
                itemList(rawItem("white_bed", 4), weight = 1),
                itemList(rawItem("white_bed", 5), weight = 2),
            ))
        },
        RandomSlotPresetGroupElement("4 to 6 Beds") {
            WeightedOptionList(mutableListOf(
                itemList(rawItem("white_bed", 4), weight = 1),
                itemList(rawItem("white_bed", 5), weight = 2),
                itemList(rawItem("white_bed", 6), weight = 1),
            ))
        },
        RandomSlotPresetGroupElement("4 to 7 Beds") {
            WeightedOptionList(mutableListOf(
                itemList(rawItem("white_bed", 4), weight = 1),
                itemList(rawItem("white_bed", 5), weight = 2),
                itemList(rawItem("white_bed", 6), weight = 2),
                itemList(rawItem("white_bed", 7), weight = 1),
            ))
        },
        RandomSlotPresetGroupElement("5 to 6 Beds") {
            WeightedOptionList(mutableListOf(
                itemList(rawItem("white_bed", 5), weight = 1),
                itemList(rawItem("white_bed", 6), weight = 1),
            ))
        },
        RandomSlotPresetGroupElement("5 to 7 Beds") {
            WeightedOptionList(mutableListOf(
                itemList(rawItem("white_bed", 5), weight = 1),
                itemList(rawItem("white_bed", 6), weight = 2),
                itemList(rawItem("white_bed", 7), weight = 1),
            ))
        },
        RandomSlotPresetGroupElement("5 to 8 Beds") {
            WeightedOptionList(mutableListOf(
                itemList(rawItem("white_bed", 5), weight = 1),
                itemList(rawItem("white_bed", 6), weight = 3),
                itemList(rawItem("white_bed", 7), weight = 2),
                itemList(rawItem("white_bed", 8), weight = 1),
            ))
        },
        RandomSlotPresetGroupElement("6 to 7 Beds") {
            WeightedOptionList(mutableListOf(
                itemList(rawItem("white_bed", 6), weight = 1),
                itemList(rawItem("white_bed", 7), weight = 1),
            ))
        },
        RandomSlotPresetGroupElement("6 to 8 Beds") {
            WeightedOptionList(mutableListOf(
                itemList(rawItem("white_bed", 6), weight = 1),
                itemList(rawItem("white_bed", 7), weight = 2),
                itemList(rawItem("white_bed", 8), weight = 1),
            ))
        },
        RandomSlotPresetGroupElement("6 to 9 Beds") {
            WeightedOptionList(mutableListOf(
                itemList(rawItem("white_bed", 6), weight = 1),
                itemList(rawItem("white_bed", 7), weight = 2),
                itemList(rawItem("white_bed", 8), weight = 2),
                itemList(rawItem("white_bed", 9), weight = 1),
            ))
        },
        RandomSlotPresetGroupElement("7 to 10 Beds") {
            WeightedOptionList(mutableListOf(
                itemList(rawItem("white_bed", 7), weight = 1),
                itemList(rawItem("white_bed", 8), weight = 2),
                itemList(rawItem("white_bed", 9), weight = 2),
                itemList(rawItem("white_bed", 10), weight = 1),
            ))
        },
    )),

    BEDS_ANCHORS("Random Amount of Beds + Anchors", rawItem("respawn_anchor"), listOf(
        RandomSlotPresetGroupElement("4 to 5 Explosives") {
            WeightedOptionList(mutableListOf(
                explosives(4, weight=1),
                explosives(4, 1, weight=1),
                explosives(4, 2, weight=1),
                explosives(4, 3, weight=1),

                explosives(5, weight=2),
                explosives(5, 1, weight=2),
                explosives(5, 2, weight=2),
                explosives(5, 3, weight=2),
            ))
        },
        RandomSlotPresetGroupElement("4 to 6 Explosives") {
            WeightedOptionList(mutableListOf(
                explosives(4, weight=1),
                explosives(4, 1, weight=1),
                explosives(4, 2, weight=1),
                explosives(4, 3, weight=1),

                explosives(5, weight=2),
                explosives(5, 1, weight=2),
                explosives(5, 2, weight=2),
                explosives(5, 3, weight=2),

                explosives(6, weight=1),
                explosives(6, 1, weight=1),
                explosives(6, 2, weight=1),
                explosives(6, 3, weight=1),

                ))
        },
        RandomSlotPresetGroupElement("4 to 7 Explosives") {
            WeightedOptionList(mutableListOf(
                explosives(4, weight=1),
                explosives(4, 1, weight=1),
                explosives(4, 2, weight=1),
                explosives(4, 3, weight=1),

                explosives(5, weight=2),
                explosives(5, 1, weight=2),
                explosives(5, 2, weight=2),
                explosives(5, 3, weight=2),

                explosives(6, weight=2),
                explosives(6, 1, weight=2),
                explosives(6, 2, weight=2),
                explosives(6, 3, weight=2),

                explosives(7, weight=1),
                explosives(7, 1, weight=1),
                explosives(7, 2, weight=1),
                explosives(7, 3, weight=1),
                explosives(7, 4, weight=1),
            ))
        },
        RandomSlotPresetGroupElement("5 to 6 Explosives") {
            WeightedOptionList(mutableListOf(
                explosives(5, weight=1),
                explosives(5, 1, weight=1),
                explosives(5, 2, weight=1),
                explosives(5, 3, weight=1),

                explosives(6, weight=1),
                explosives(6, 1, weight=1),
                explosives(6, 2, weight=1),
                explosives(6, 3, weight=1),
            ))
        },
        RandomSlotPresetGroupElement("5 to 7 Explosives") {
            WeightedOptionList(mutableListOf(
                explosives(5, weight=1),
                explosives(5, 1, weight=1),
                explosives(5, 2, weight=1),
                explosives(5, 3, weight=1),

                explosives(6, weight=2),
                explosives(6, 1, weight=2),
                explosives(6, 2, weight=2),
                explosives(6, 3, weight=2),

                explosives(7, weight=1),
                explosives(7, 1, weight=1),
                explosives(7, 2, weight=1),
                explosives(7, 3, weight=1),
                explosives(7, 4, weight=1),
            ))
        },
        RandomSlotPresetGroupElement("5 to 8 Explosives") {
            WeightedOptionList(mutableListOf(
                explosives(5, weight=1),
                explosives(5, 1, weight=1),
                explosives(5, 2, weight=1),
                explosives(5, 3, weight=1),

                explosives(6, weight=3),
                explosives(6, 1, weight=3),
                explosives(6, 2, weight=3),
                explosives(6, 3, weight=3),

                explosives(7, weight=2),
                explosives(7, 1, weight=2),
                explosives(7, 2, weight=2),
                explosives(7, 3, weight=2),
                explosives(7, 4, weight=2),

                explosives(8, weight=1),
                explosives(8, 1, weight=1),
                explosives(8, 2, weight=1),
                explosives(8, 3, weight=1),
                explosives(8, 4, weight=1),
            ))
        },
        RandomSlotPresetGroupElement("6 to 7 Explosives") {
            WeightedOptionList(mutableListOf(
                explosives(6, weight=1),
                explosives(6, 1, weight=1),
                explosives(6, 2, weight=1),
                explosives(6, 3, weight=1),

                explosives(7, weight=1),
                explosives(7, 1, weight=1),
                explosives(7, 2, weight=1),
                explosives(7, 3, weight=1),
                explosives(7, 4, weight=1),
            ))
        },
        RandomSlotPresetGroupElement("6 to 8 Explosives") {
            WeightedOptionList(mutableListOf(
                explosives(6, weight=1),
                explosives(6, 1, weight=1),
                explosives(6, 2, weight=1),
                explosives(6, 3, weight=1),

                explosives(7, weight=2),
                explosives(7, 1, weight=2),
                explosives(7, 2, weight=2),
                explosives(7, 3, weight=2),
                explosives(7, 4, weight=2),

                explosives(8, weight=1),
                explosives(8, 1, weight=1),
                explosives(8, 2, weight=1),
                explosives(8, 3, weight=1),
                explosives(8, 4, weight=1),
            ))
        },
        RandomSlotPresetGroupElement("6 to 9 Explosives") {
            WeightedOptionList(mutableListOf(
                explosives(6, weight=1),
                explosives(6, 1, weight=1),
                explosives(6, 2, weight=1),
                explosives(6, 3, weight=1),

                explosives(7, weight=2),
                explosives(7, 1, weight=2),
                explosives(7, 2, weight=2),
                explosives(7, 3, weight=2),
                explosives(7, 4, weight=2),

                explosives(8, weight=2),
                explosives(8, 1, weight=2),
                explosives(8, 2, weight=2),
                explosives(8, 3, weight=2),
                explosives(8, 4, weight=2),

                explosives(9, weight=1),
                explosives(9, 1, weight=1),
                explosives(9, 2, weight=1),
                explosives(9, 3, weight=1),
                explosives(9, 4, weight=1),
            ))
        },
        RandomSlotPresetGroupElement("7 to 10 Explosives") {
            WeightedOptionList(mutableListOf(
                explosives(7, weight=1),
                explosives(7, 1, weight=1),
                explosives(7, 2, weight=1),
                explosives(7, 3, weight=1),
                explosives(7, 4, weight=1),

                explosives(8, weight=2),
                explosives(8, 1, weight=2),
                explosives(8, 2, weight=2),
                explosives(8, 3, weight=2),
                explosives(8, 4, weight=2),

                explosives(9, weight=2),
                explosives(9, 1, weight=2),
                explosives(9, 2, weight=2),
                explosives(9, 3, weight=2),
                explosives(9, 4, weight=2),

                explosives(10, weight=1),
                explosives(10, 1, weight=1),
                explosives(10, 2, weight=1),
                explosives(10, 3, weight=1),
                explosives(10, 4, weight=1),
            ))
        },
    )),

    STRING("Random Amount of String", rawItem("string"), listOf(
        RandomSlotPresetGroupElement("Enough For 4 to 5 Beds") {
            WeightedOptionList(mutableListOf(
                randomString(4, weight = 1),
                randomString(5, weight = 2),
            ))
        },
        RandomSlotPresetGroupElement("Enough For 4 to 6 Beds") {
            WeightedOptionList(mutableListOf(
                randomString(4, weight = 1),
                randomString(5, weight = 2),
                randomString(6, weight = 1),
            ))
        },
        RandomSlotPresetGroupElement("Enough For 4 to 7 Beds") {
            WeightedOptionList(mutableListOf(
                randomString(4, weight = 1),
                randomString(5, weight = 2),
                randomString(6, weight = 2),
                randomString(7, weight = 1),
            ))
        },
        RandomSlotPresetGroupElement("Enough For 5 to 6 Beds") {
            WeightedOptionList(mutableListOf(
                randomString(5, weight = 1),
                randomString(6, weight = 1),
            ))
        },
        RandomSlotPresetGroupElement("Enough For 5 to 7 Beds") {
            WeightedOptionList(mutableListOf(
                randomString(5, weight = 1),
                randomString(6, weight = 2),
                randomString(7, weight = 1),
            ))
        },
        RandomSlotPresetGroupElement("Enough For 5 to 8 Beds") {
            WeightedOptionList(mutableListOf(
                randomString(5, weight = 1),
                randomString(6, weight = 3),
                randomString(7, weight = 2),
                randomString(8, weight = 1),
            ))
        },
        RandomSlotPresetGroupElement("Enough For 6 to 7 Beds") {
            WeightedOptionList(mutableListOf(
                randomString(6, weight = 1),
                randomString(7, weight = 1),
            ))
        },
        RandomSlotPresetGroupElement("Enough For 6 to 8 Beds") {
            WeightedOptionList(mutableListOf(
                randomString(6, weight = 1),
                randomString(7, weight = 2),
                randomString(8, weight = 1),
            ))
        },
        RandomSlotPresetGroupElement("Enough For 6 to 9 Beds") {
            WeightedOptionList(mutableListOf(
                randomString(6, weight = 1),
                randomString(7, weight = 2),
                randomString(8, weight = 2),
                randomString(9, weight = 1),
            ))
        },
        RandomSlotPresetGroupElement("Enough For 7 to 10 Beds") {
            WeightedOptionList(mutableListOf(
                randomString(7, weight = 1),
                randomString(8, weight = 2),
                randomString(9, weight = 2),
                randomString(10, weight = 1),
            ))
        },
    )),

    EXPLOSIVE_INGREDIENTS("Random Amount of String+Crying+Glowstone", rawItem("string"), listOf(
        RandomSlotPresetGroupElement("Enough For 4 to 5 Explosives") {
            WeightedOptionList(mutableListOf(
                explosiveIngredients(4, weight=1),
                explosiveIngredients(4, 1, weight=1),
                explosiveIngredients(4, 2, weight=1),
                explosiveIngredients(4, 3, weight=1),

                explosiveIngredients(5, weight=2),
                explosiveIngredients(5, 1, weight=2),
                explosiveIngredients(5, 2, weight=2),
                explosiveIngredients(5, 3, weight=2),
            ))
        },
        RandomSlotPresetGroupElement("Enough For 4 to 6 Explosives") {
            WeightedOptionList(mutableListOf(
                explosiveIngredients(4, weight=1),
                explosiveIngredients(4, 1, weight=1),
                explosiveIngredients(4, 2, weight=1),
                explosiveIngredients(4, 3, weight=1),

                explosiveIngredients(5, weight=2),
                explosiveIngredients(5, 1, weight=2),
                explosiveIngredients(5, 2, weight=2),
                explosiveIngredients(5, 3, weight=2),

                explosiveIngredients(6, weight=1),
                explosiveIngredients(6, 1, weight=1),
                explosiveIngredients(6, 2, weight=1),
                explosiveIngredients(6, 3, weight=1),

                ))
        },
        RandomSlotPresetGroupElement("Enough For 4 to 7 Explosives") {
            WeightedOptionList(mutableListOf(
                explosiveIngredients(4, weight=1),
                explosiveIngredients(4, 1, weight=1),
                explosiveIngredients(4, 2, weight=1),
                explosiveIngredients(4, 3, weight=1),

                explosiveIngredients(5, weight=2),
                explosiveIngredients(5, 1, weight=2),
                explosiveIngredients(5, 2, weight=2),
                explosiveIngredients(5, 3, weight=2),

                explosiveIngredients(6, weight=2),
                explosiveIngredients(6, 1, weight=2),
                explosiveIngredients(6, 2, weight=2),
                explosiveIngredients(6, 3, weight=2),

                explosiveIngredients(7, weight=1),
                explosiveIngredients(7, 1, weight=1),
                explosiveIngredients(7, 2, weight=1),
                explosiveIngredients(7, 3, weight=1),
                explosiveIngredients(7, 4, weight=1),
            ))
        },
        RandomSlotPresetGroupElement("Enough For 5 to 6 Explosives") {
            WeightedOptionList(mutableListOf(
                explosiveIngredients(5, weight=1),
                explosiveIngredients(5, 1, weight=1),
                explosiveIngredients(5, 2, weight=1),
                explosiveIngredients(5, 3, weight=1),

                explosiveIngredients(6, weight=1),
                explosiveIngredients(6, 1, weight=1),
                explosiveIngredients(6, 2, weight=1),
                explosiveIngredients(6, 3, weight=1),
            ))
        },
        RandomSlotPresetGroupElement("Enough For 5 to 7 Explosives") {
            WeightedOptionList(mutableListOf(
                explosiveIngredients(5, weight=1),
                explosiveIngredients(5, 1, weight=1),
                explosiveIngredients(5, 2, weight=1),
                explosiveIngredients(5, 3, weight=1),

                explosiveIngredients(6, weight=2),
                explosiveIngredients(6, 1, weight=2),
                explosiveIngredients(6, 2, weight=2),
                explosiveIngredients(6, 3, weight=2),

                explosiveIngredients(7, weight=1),
                explosiveIngredients(7, 1, weight=1),
                explosiveIngredients(7, 2, weight=1),
                explosiveIngredients(7, 3, weight=1),
                explosiveIngredients(7, 4, weight=1),
            ))
        },
        RandomSlotPresetGroupElement("Enough For 5 to 8 Explosives") {
            WeightedOptionList(mutableListOf(
                explosiveIngredients(5, weight=1),
                explosiveIngredients(5, 1, weight=1),
                explosiveIngredients(5, 2, weight=1),
                explosiveIngredients(5, 3, weight=1),

                explosiveIngredients(6, weight=3),
                explosiveIngredients(6, 1, weight=3),
                explosiveIngredients(6, 2, weight=3),
                explosiveIngredients(6, 3, weight=3),

                explosiveIngredients(7, weight=2),
                explosiveIngredients(7, 1, weight=2),
                explosiveIngredients(7, 2, weight=2),
                explosiveIngredients(7, 3, weight=2),
                explosiveIngredients(7, 4, weight=2),

                explosiveIngredients(8, weight=1),
                explosiveIngredients(8, 1, weight=1),
                explosiveIngredients(8, 2, weight=1),
                explosiveIngredients(8, 3, weight=1),
                explosiveIngredients(8, 4, weight=1),
            ))
        },
        RandomSlotPresetGroupElement("Enough For 6 to 7 Explosives") {
            WeightedOptionList(mutableListOf(
                explosiveIngredients(6, weight=1),
                explosiveIngredients(6, 1, weight=1),
                explosiveIngredients(6, 2, weight=1),
                explosiveIngredients(6, 3, weight=1),

                explosiveIngredients(7, weight=1),
                explosiveIngredients(7, 1, weight=1),
                explosiveIngredients(7, 2, weight=1),
                explosiveIngredients(7, 3, weight=1),
                explosiveIngredients(7, 4, weight=1),
            ))
        },
        RandomSlotPresetGroupElement("Enough For 6 to 8 Explosives") {
            WeightedOptionList(mutableListOf(
                explosiveIngredients(6, weight=1),
                explosiveIngredients(6, 1, weight=1),
                explosiveIngredients(6, 2, weight=1),
                explosiveIngredients(6, 3, weight=1),

                explosiveIngredients(7, weight=2),
                explosiveIngredients(7, 1, weight=2),
                explosiveIngredients(7, 2, weight=2),
                explosiveIngredients(7, 3, weight=2),
                explosiveIngredients(7, 4, weight=2),

                explosiveIngredients(8, weight=1),
                explosiveIngredients(8, 1, weight=1),
                explosiveIngredients(8, 2, weight=1),
                explosiveIngredients(8, 3, weight=1),
                explosiveIngredients(8, 4, weight=1),
            ))
        },
        RandomSlotPresetGroupElement("Enough For 6 to 9 Explosives") {
            WeightedOptionList(mutableListOf(
                explosiveIngredients(6, weight=1),
                explosiveIngredients(6, 1, weight=1),
                explosiveIngredients(6, 2, weight=1),
                explosiveIngredients(6, 3, weight=1),

                explosiveIngredients(7, weight=2),
                explosiveIngredients(7, 1, weight=2),
                explosiveIngredients(7, 2, weight=2),
                explosiveIngredients(7, 3, weight=2),
                explosiveIngredients(7, 4, weight=2),

                explosiveIngredients(8, weight=2),
                explosiveIngredients(8, 1, weight=2),
                explosiveIngredients(8, 2, weight=2),
                explosiveIngredients(8, 3, weight=2),
                explosiveIngredients(8, 4, weight=2),

                explosiveIngredients(9, weight=1),
                explosiveIngredients(9, 1, weight=1),
                explosiveIngredients(9, 2, weight=1),
                explosiveIngredients(9, 3, weight=1),
                explosiveIngredients(9, 4, weight=1),
            ))
        },
        RandomSlotPresetGroupElement("Enough For 7 to 10 Explosives") {
            WeightedOptionList(mutableListOf(
                explosiveIngredients(7, weight=1),
                explosiveIngredients(7, 1, weight=1),
                explosiveIngredients(7, 2, weight=1),
                explosiveIngredients(7, 3, weight=1),
                explosiveIngredients(7, 4, weight=1),

                explosiveIngredients(8, weight=2),
                explosiveIngredients(8, 1, weight=2),
                explosiveIngredients(8, 2, weight=2),
                explosiveIngredients(8, 3, weight=2),
                explosiveIngredients(8, 4, weight=2),

                explosiveIngredients(9, weight=2),
                explosiveIngredients(9, 1, weight=2),
                explosiveIngredients(9, 2, weight=2),
                explosiveIngredients(9, 3, weight=2),
                explosiveIngredients(9, 4, weight=2),

                explosiveIngredients(10, weight=1),
                explosiveIngredients(10, 1, weight=1),
                explosiveIngredients(10, 2, weight=1),
                explosiveIngredients(10, 3, weight=1),
                explosiveIngredients(10, 4, weight=1),
            ))
        },
    )),
}

data class RandomSlotPresetGroupElement(
    val displayName: String,
    val optionsGetter: () -> WeightedOptionList<SnapshotStateList<MinecraftItem>>
)