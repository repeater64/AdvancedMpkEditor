package me.repeater64.advancedmpkeditor.backend.data_object.item

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable

data class LootingSwordItem(
    val lootingLevel: Int,
) : MinecraftItem {
    override fun contentHash() = hashCode() // Can use this for immutable data class
    override val commandString = "golden_sword{Enchantments:[{lvl:$lootingLevel,id:looting}]} 1"
    override val displayName = "Looting ${lootingLevel} Golden Sword"
    override val amount = 1
    override val iconFile = when (lootingLevel) {
            1 -> "golden_sword_l1.png"
            2 -> "golden_sword_l2.png"
            else -> "golden_sword_l3.png"
    }
    override val companion = Companion as BookSerializable<MinecraftItem>
    override val numStacks = 1
    override val stackSize = 1

    override fun equalsIgnoringAmount(other: MinecraftItem): Boolean {
        return (other is LootingSwordItem && this.lootingLevel == other.lootingLevel)
    }

    companion object : BookSerializable<LootingSwordItem> {
        override val className = "LootingSwordItem"

        override fun serializeToPages(it: LootingSwordItem): List<String> {
            return listOf(it.lootingLevel.toString())
        }

        override fun deserializeFromPages(pages: List<String>): LootingSwordItem {
            return LootingSwordItem(pages[0].toInt())
        }
    }

}