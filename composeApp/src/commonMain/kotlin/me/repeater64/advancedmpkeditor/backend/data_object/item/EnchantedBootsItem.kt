package me.repeater64.advancedmpkeditor.backend.data_object.item

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable

data class EnchantedBootsItem(
    val iron: Boolean,
    val ssLevel: Int,
) : MinecraftItem {
    override fun contentHash() = hashCode() // Can use this for immutable data class

    override val commandString = "${if (iron) "iron" else "golden"}_boots{Enchantments:[{lvl:$ssLevel,id:soul_speed}]} 1"
    override val displayName = if (iron) "Iron Boots (Soul Speed ${integer123ToRomanNumerals(ssLevel)})" else "Golden Boots (Soul Speed ${integer123ToRomanNumerals(ssLevel)})"
    override val amount = 1
    override val iconFile = if (iron) {
        when (ssLevel) {
            1 -> "iron_boots_ss1.png"
            2 -> "iron_boots_ss2.png"
            else -> "iron_boots_ss3.png"
        }
    } else {
        when (ssLevel) {
            1 -> "golden_boots_ss1.png"
            2 -> "golden_boots_ss2.png"
            else -> "golden_boots_ss3.png"
        }
    }
    override val companion = Companion as BookSerializable<MinecraftItem>
    override val numStacks = 1
    override val stackSize = 1

    override fun equalsIgnoringAmount(other: MinecraftItem): Boolean {
        return (other is EnchantedBootsItem && this.iron == other.iron && this.ssLevel == other.ssLevel)
    }

    companion object : BookSerializable<EnchantedBootsItem> {
        fun integer123ToRomanNumerals(integer: Int) : String {
            return when (integer) {
                1 -> "I"
                2 -> "II"
                3 -> "III"
                else -> "INVALID LEVEL"
            }
        }


        override val className = "EnchantedBootsItem"

        override fun serializeToPages(it: EnchantedBootsItem): List<String> {
            return listOf(it.iron.toString(), it.ssLevel.toString())
        }

        override fun deserializeFromPages(pages: List<String>): EnchantedBootsItem {
            return EnchantedBootsItem(pages[0].toBoolean(), pages[1].toInt())
        }
    }

}