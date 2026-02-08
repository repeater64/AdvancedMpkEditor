package me.repeater64.advancedmpkeditor.backend.data_object.misc_options

enum class DifficultyOption(override val minecraftItemId: String, override val displayName: String, override val amount: Int = 1) : ItemBasedOptionEnum {
    PEACEFUL("minecraft:leather_helmet", "Peaceful"),
    EASY("minecraft:golden_helmet", "Easy"),
    NORMAL("minecraft:iron_helmet", "Normal"),
    HARD("minecraft:diamond_helmet", "Hard"),

}