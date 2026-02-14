package me.repeater64.advancedmpkeditor.backend.data_object.item

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializableNoAttributes
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.NoAttributesDataClass

class ForcedEmptyMinecraftItem : NoAttributesDataClass(), MinecraftItem {
    override val amount: Int = 1
    override val commandString: String
        get() {
            return "minecraft:barrier{\"id\":${identifier++}}"
        }
    override val displayName = "Forced Empty Slot"
    override val iconFile = "barrier.png"
    override val numStacks = 1
    override val stackSize = 1
    override val companion = Companion as BookSerializable<MinecraftItem>

    companion object : BookSerializableNoAttributes<ForcedEmptyMinecraftItem> {
        override fun createObject() = ForcedEmptyMinecraftItem()
        override val className = "ForcedEmptyMinecraftItem"

        var identifier: Int = 100
    }
}