package me.repeater64.advancedmpkeditor.backend.data_object.item

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializableNoAttributes
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.NoAttributesDataClass

class KillZombiePigsPotionItem : NoAttributesDataClass(), MinecraftItem {
    override val commandEndBit = "lingering_potion{pages:[\"kill @e[type=minecraft:zombified_piglin]\",\"say Killed zombie pigs!\"],display:{Name:'{\"text\":\"Kill Zombie Pigs\"}',Lore:['{\"text\":\"Kill all zombified piglins\"}']},CustomPotionColor:1481884,HideFlags:255} 1"
    override val displayName = "\"Kill Zombie Pigs\" MPK Potion"
    override val amount = 1
    override val iconFile = "kill_zombie_pigs_potion.png"
    override val numStacks = 1
    override val stackSize = 1
    override val companion = Companion as BookSerializable<MinecraftItem>

    companion object : BookSerializableNoAttributes<KillZombiePigsPotionItem> {
        override fun createObject() = KillZombiePigsPotionItem()
        override val className = "KillZombiePigsPotionItem"
    }
}