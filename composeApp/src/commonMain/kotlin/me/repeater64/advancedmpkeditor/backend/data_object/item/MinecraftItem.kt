package me.repeater64.advancedmpkeditor.backend.data_object.item

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable

interface MinecraftItem {
    val commandString: String
    val displayName: String
    val amount: Int
    val iconFile: String
    val companion: BookSerializable<MinecraftItem>

    val numStacks: Int

    fun getCommandStringNonStackable(num: Int): String = commandString

    companion object : BookSerializable<MinecraftItem> {
        override val className = "MinecraftItem"

        val registry by lazy { mapOf(
            DontReplaceMinecraftItem.className to DontReplaceMinecraftItem,
            SimpleMinecraftItem.className to SimpleMinecraftItem,
            ForcedEmptyMinecraftItem.className to ForcedEmptyMinecraftItem,
            FireResItem.className to FireResItem,
            SplashFireResItem.className to SplashFireResItem,
            EnchantedBootsItem.className to EnchantedBootsItem
        ) }

        override fun serializeToPages(it: MinecraftItem): List<String> {
            return listOf(it.companion.className) + it.companion.serializeToPages(it)
        }

        override fun deserializeFromPages(pages: List<String>): MinecraftItem {
            val companion = registry[pages[0]] ?: throw IllegalArgumentException("Invalid serialized MinecraftItem type: ${pages[0]}!")

            return companion.deserializeFromPages(pages.drop(1))
        }
    }
}