package me.repeater64.advancedmpkeditor.backend.data_object.item

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializableNoAttributes
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.NoAttributesDataClass

class SplashFireResItem : NoAttributesDataClass(), MinecraftItem {
    override val commandString = "splash_potion{Potion:\"minecraft:fire_resistance\"} 1"
    override val displayName = "Potion of Fire Resistance"
    override val amount = 1
    override val iconFile = "splash_fire_res.png"
    override val numStacks = 1
    override val stackSize = 1
    override val companion = Companion as BookSerializable<MinecraftItem>

    companion object : BookSerializableNoAttributes<SplashFireResItem> {
        override fun createObject() = SplashFireResItem()
        override val className = "SplashFireResItem"
    }
}