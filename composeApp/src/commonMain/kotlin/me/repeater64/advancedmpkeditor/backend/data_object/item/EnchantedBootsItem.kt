package me.repeater64.advancedmpkeditor.backend.data_object.item

data class EnchantedBootsItem(
    val iron: Boolean,
    val ssLevel: Int,
) : MinecraftItem {
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

    companion object : MinecraftItemFactory<EnchantedBootsItem> {
        fun integer123ToRomanNumerals(integer: Int) : String {
            return when (integer) {
                1 -> "I"
                2 -> "II"
                3 -> "III"
                else -> "INVALID LEVEL"
            }
        }


        override val pattern = "soul_speed"
        override fun create(command: String) = EnchantedBootsItem(command.contains("iron"), command.split("lvl:")[1].split(",")[0].toInt())
    }

}