package me.repeater64.advancedmpkeditor.backend.data_object.item

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializableNoAttributes
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.NoAttributesDataClass

class FireResItem : NoAttributesDataClass(), MinecraftItem {
    override val commandString = "potion{CustomPotionColor:16351261,CustomPotionEffects:[{Id:12,Duration:3600}],display:{Name:'[{\"text\":\"Potion of Fire Resistance\",\"italic\":false}]'}} 1"
    override val displayName = "Potion of Fire Resistance"
    override val amount = 1
    override val iconFile = "fire_res.png"
    override val numStacks = 1
    override val companion = Companion as BookSerializable<MinecraftItem>

    companion object : BookSerializableNoAttributes<FireResItem> {
        override fun createObject() = FireResItem()
        override val className = "FireResItem"
    }
}