package me.repeater64.advancedmpkeditor.backend.data_object.misc_options

enum class GamemodeOption(override val minecraftItemId: String, override val displayName: String, override val amount: Int = 1) : ItemBasedOptionEnum {
    SURVIVAL("minecraft:iron_sword", "Survival"),
    ADVENTURE("minecraft:map", "Adventure"),
    CREATIVE("minecraft:grass_block", "Creative"),
    SPECTATOR("minecraft:ender_eye", "Spectator"),

}