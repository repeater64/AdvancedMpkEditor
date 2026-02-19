package me.repeater64.advancedmpkeditor.backend.data_object.item

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.item.EnchantedBootsItem.Companion.integer123ToRomanNumerals

data class SoulSpeedBookItem(
    val ssLevel: Int,
) : MinecraftItem {
    override fun contentHash() = hashCode() // Can use this for immutable data class

    override val commandEndBit = "enchanted_book{StoredEnchantments:[{lvl:${ssLevel}s, id:\"minecraft:soul_speed\"}]} 1"
    override val displayName = "Soul Speed ${integer123ToRomanNumerals(ssLevel)} Book"
    override val amount = 1
    override val iconFile = "enchanted_book.png"
    override val companion = Companion as BookSerializable<MinecraftItem>
    override val numStacks = 1
    override val stackSize = 1

    override fun equalsIgnoringAmount(other: MinecraftItem): Boolean {
        return (other is SoulSpeedBookItem && this.ssLevel == other.ssLevel)
    }

    companion object : BookSerializable<SoulSpeedBookItem> {
        override val className = "EnchantedBootsItem"

        override fun serializeToPages(it: SoulSpeedBookItem): List<String> {
            return listOf(it.ssLevel.toString())
        }

        override fun deserializeFromPages(pages: List<String>): SoulSpeedBookItem {
            return SoulSpeedBookItem(pages[0].toInt())
        }
    }

}