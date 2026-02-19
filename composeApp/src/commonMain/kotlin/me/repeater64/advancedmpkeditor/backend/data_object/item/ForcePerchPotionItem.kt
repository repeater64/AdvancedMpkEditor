package me.repeater64.advancedmpkeditor.backend.data_object.item

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializableNoAttributes
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.NoAttributesDataClass

class ForcePerchPotionItem : NoAttributesDataClass(), MinecraftItem {
    override val commandEndBit = "lingering_potion{pages:[\"data merge entity @e[type=ender_dragon,limit=1] {DragonPhase:2}\",\"say Forcing perch!\"],display:{Name:'{\"text\":\"Force Perch\"}',Lore:['{\"text\":\"Force the dragon to perch\"}']},CustomPotionColor:13238277,HideFlags:255} 1"
    override val displayName = "\"Force Perch\" MPK Potion"
    override val amount = 1
    override val iconFile = "force_perch_potion.png"
    override val numStacks = 1
    override val stackSize = 1
    override val companion = Companion as BookSerializable<MinecraftItem>

    companion object : BookSerializableNoAttributes<ForcePerchPotionItem> {
        override fun createObject() = ForcePerchPotionItem()
        override val className = "ForcePerchPotionItem"
    }
}