package me.repeater64.advancedmpkeditor.backend.data_object.item

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializableNoAttributes
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.NoAttributesDataClass

class DontReplaceMinecraftItem : NoAttributesDataClass(), MinecraftItem {
    override val amount: Int = 1
    override val commandString = "intentionally_invalid_command"
    override val displayName = "Available for Random Items"
    override val iconFile = "blank.png"
    override val numStacks = 0
    override val companion = Companion as BookSerializable<MinecraftItem>

    companion object : BookSerializableNoAttributes<DontReplaceMinecraftItem> {
        override fun createObject() = DontReplaceMinecraftItem()
        override val className = "DontReplaceMinecraftItem"
    }
}