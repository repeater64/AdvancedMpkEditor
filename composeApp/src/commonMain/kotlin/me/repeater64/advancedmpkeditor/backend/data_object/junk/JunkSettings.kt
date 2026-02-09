package me.repeater64.advancedmpkeditor.backend.data_object.junk

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem

data class JunkSettings(
    val enableJunk: Boolean,
    val makeJunkNonStackable: Boolean,
    val junkList: List<MinecraftItem>
) {
    companion object : BookSerializable<JunkSettings> {
        override val className = "JunkSettings"

        override fun serializeToPages(it: JunkSettings): List<String> {
            return listOf(it.enableJunk.toString(), it.makeJunkNonStackable.toString()) + BookSerializable.serializeList(it.junkList, MinecraftItem)
        }

        override fun deserializeFromPages(pages: List<String>): JunkSettings {
            return JunkSettings(pages[0].toBoolean(), pages[1].toBoolean(), BookSerializable.getListAndRemainingPages(pages.drop(2), MinecraftItem).first)
        }
    }
}