package me.repeater64.advancedmpkeditor.backend.data_object.health_hunger

enum class HungerOption(val displayName: String, val commands: List<String>) {
    DOWN_8_HUNGER("Down 8 Hunger", listOf("effect give @p hunger 2 255")),
    DOWN_6_HUNGER("Down 6 Hunger", listOf("effect give @p hunger 2 230")),
    DOWN_4_HUNGER("Down 4 Hunger", listOf("effect give @p hunger 2 180")),
    DOWN_2_HUNGER("Down 2 Hunger", listOf("effect give @p hunger 2 140")),
    FULL_HUNGER_NO_SAT("Full Hunger, No Saturation", listOf("effect give @p hunger 1 200")),
    ROTTEN_FLESH("1 Saturation (Rotten Flesh)", listOf("effect give @p hunger 1 180")),
    APPLE("2 Saturation (Apple)", listOf("effect give @p hunger 1 140")),
    HUNGER_RESET("5 Saturation (Hunger Reset)", emptyList()),
    BREAD("6 Saturation (Bread, Cod)", listOf("effect give @p hunger 1 50","effect give @p saturation 1 0")),
    CHICKEN("7 Saturation (Chicken, Stew)", listOf("effect give @p saturation 1 0")),
    MUTTON("10 Saturation (Mutton, Salmon)", listOf("effect give @p hunger 1 50","effect give @p saturation 1 2")),
    BEEF("13 Saturation (Beef, Pork)", listOf("effect give @p saturation 1 3")),
    GOLDEN_CARROT("14 Saturation (Golden Carrot)", listOf("effect give @p saturation 1 4", "effect give @p hunger 1 50")),
}