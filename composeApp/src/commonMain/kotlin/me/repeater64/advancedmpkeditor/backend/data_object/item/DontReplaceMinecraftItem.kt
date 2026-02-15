package me.repeater64.advancedmpkeditor.backend.data_object.item

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializableNoAttributes
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.NoAttributesDataClass

class DontReplaceMinecraftItem(callNothing: Boolean = false)
    : NoAttributesDataClass() // Doesn't have any actual attributes, whether or not it is called Nothing doesn't change what it is, it's just for GUI clarity in different places
    , MinecraftItem {
    override val amount: Int = 1
    override val commandString = "intentionally_invalid_command"
    override val displayName = if (callNothing) "Nothing" else "Available for Random Items"
    override val iconFile = "barrier.png"
    override val numStacks = 0
    override val stackSize = 1
    override val companion = Companion as BookSerializable<MinecraftItem>

    companion object : BookSerializableNoAttributes<DontReplaceMinecraftItem> {
        override fun createObject() = DontReplaceMinecraftItem()
        override val className = "DontReplaceMinecraftItem"
    }
}