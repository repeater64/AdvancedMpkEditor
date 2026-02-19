package me.repeater64.advancedmpkeditor.backend.data_object.item

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializableNoAttributes
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.NoAttributesDataClass

data class RandomBarterItem(override val amount: Int = 1) : MinecraftItemWithAmount {
    override fun contentHash() = hashCode() // Can use this for immutable data class

    override val commandEndBit = ""
    override val displayName = "Random Piglin Barter"
    override val iconFile = "piglin_icon.png"
    override val numStacks = amount
    override val stackSize = 1
    override val companion = Companion as BookSerializable<MinecraftItem>

    override fun equalsIgnoringAmount(other: MinecraftItem): Boolean {
        return (other is RandomBarterItem)
    }

    override fun copyWithAmount(amount: Int) = RandomBarterItem(amount)

    override fun getReplaceitemCommand(slotString: String): String {
        return "loot replace entity @p $slotString 1 loot gameplay/piglin_bartering"
    }

    override fun getGiveCommands(makeNonStackableNum: Int?): List<String> {
        val list = mutableListOf<String>()
        repeat(amount) {
            list.add("loot give @p loot gameplay/piglin_bartering")
        }
        return list
    }

    companion object : BookSerializable<RandomBarterItem> {
        override val className = "RandomBarterItem"
        override fun serializeToPages(it: RandomBarterItem): List<String> {
            return listOf(it.amount.toString())
        }

        override fun deserializeFromPages(pages: List<String>): RandomBarterItem {
            return RandomBarterItem(pages[0].toInt())
        }
    }
}