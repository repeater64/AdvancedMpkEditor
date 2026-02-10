package me.repeater64.advancedmpkeditor.backend.data_object.health_hunger

enum class HealthOption(val displayName: String, val commands: List<String>) {
    FULL_HEALTH("Full Health", emptyList()),
    DOWN_3_HEARTS("Down 3 Hearts", listOf("effect give @p instant_damage 1 0")),
    DOWN_6_HEARTS("Down 6 Hearts", listOf("effect give @p instant_damage 1 1")),
    DOWN_9_HEARTS("Down 9 Hearts (Delays teleport by 0.5s)", listOf("effect give @p instant_damage 1 1", "scoreboard players set @p extradmg 1"))
}