package me.repeater64.advancedmpkeditor.backend.data_object.item

interface MinecraftItem {
    val commandString: String
    val displayName: String
    val amount: Int
    val iconFile: String

    companion object {
        private val registry by lazy { listOf(
            FireResItem,
            SplashFireResItem,
            DontReplaceMinecraftItem,
            ForcedEmptyMinecraftItem,
            EnchantedBootsItem,

            SimpleMinecraftItem // Check this last, as it will match all patterns
        ) }

        fun fromCommandString(commandString: String): MinecraftItem {
            val factory = registry.find { it.matches(commandString) }
                ?: throw IllegalArgumentException("No item class found for command string: $commandString")

            return factory.create(commandString)
        }
    }
}

interface MinecraftItemFactory<out T : MinecraftItem> {
    val pattern: String
    fun create(command: String): T

    fun matches(command: String): Boolean = command.contains(pattern)
}