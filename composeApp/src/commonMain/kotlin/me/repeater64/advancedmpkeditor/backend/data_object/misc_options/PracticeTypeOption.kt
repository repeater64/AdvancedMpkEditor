package me.repeater64.advancedmpkeditor.backend.data_object.misc_options

enum class PracticeTypeOption(override val minecraftItemId: String, override val displayName: String, override val amount: Int = 1) : ItemBasedOptionEnum {
    BURIED_TREASURE("minecraft:heart_of_the_sea", "Buried Treasure"),
    SHIPWRECK("minecraft:oak_boat", "Shipwreck"),
    OCEAN_MONUMENT("minecraft:prismarine", "Ocean Monument"),
    NETHER_ENTER("minecraft:netherrack", "Nether Enter"),
    BASTION("minecraft:gilded_blackstone", "Bastion"),
    FORTRESS("minecraft:blaze_rod", "Fortress"),
    BLIND("minecraft:obsidian", "Blind"),
    BLIND_COORDS_PORTAL_BUILT("minecraft:obsidian", "Blind Coords (Portal Built)", 2),
    BLIND_COORDS_PORTAL_NOT_BUILT("minecraft:obsidian", "Blind Coords (Portal Not Built)", 3),
    STRONGHOLD("minecraft:end_portal_frame", "Stronghold Starter"),
    END_ENTER("minecraft:end_stone", "End Enter")
}