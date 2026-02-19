package me.repeater64.advancedmpkeditor.backend.data_object.misc_options

enum class PracticeTypeOption(override val minecraftItemId: String, override val displayName: String, val isBlindOrLater: Boolean = false, override val amount: Int = 1) : ItemBasedOptionEnum {
    BURIED_TREASURE("minecraft:heart_of_the_sea", "Buried Treasure"),
    SHIPWRECK("minecraft:oak_boat", "Shipwreck"),
    OCEAN_MONUMENT("minecraft:prismarine", "Ocean Monument"),
    NETHER_ENTER("minecraft:netherrack", "Nether Enter"),
    BASTION("minecraft:gilded_blackstone", "Bastion"),
    FORTRESS("minecraft:blaze_rod", "Fortress"),
    BLIND("minecraft:obsidian", "Blind", isBlindOrLater = true),
    BLIND_COORDS_PORTAL_BUILT("minecraft:obsidian", "Blind Coords (Portal Built)", isBlindOrLater = true, 2),
    BLIND_COORDS_PORTAL_NOT_BUILT("minecraft:obsidian", "Blind Coords (Portal Not Built)", isBlindOrLater = true, 3),
    STRONGHOLD("minecraft:end_portal_frame", "Stronghold Starter", isBlindOrLater = true),
    STRONGHOLD_NO_PORTAL("minecraft:end_portal_frame", "Stronghold Starter (No Portal Built)", isBlindOrLater = true, amount=2), // Amount 2 is just to distinguish from the other option
    END_ENTER("minecraft:end_stone", "End Enter", isBlindOrLater = true)
}