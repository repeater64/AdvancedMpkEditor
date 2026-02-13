package me.repeater64.advancedmpkeditor.backend.data_object.item

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable

data class SimpleMinecraftItem(private val id: String, override val amount: Int) : MinecraftItem {
    override fun contentHash() = hashCode() // Can use this for immutable data class

    override val commandString = "$id $amount"
    override val displayName = id.split("_").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
    override val iconFile = "${id}.png"

    override val companion = Companion as BookSerializable<MinecraftItem>

    override val numStacks: Int
        get() {
            return amount / maxStackSize + if (amount % maxStackSize > 0) 1 else 0
        }

    override fun getCommandStringNonStackable(num: Int) = "${id}{a:$num} $amount"
    private val maxStackSize: Int
        get() {
            val item = id.removePrefix("minecraft:")
            if (item.endsWith("_sign")) return 16
            if (item.endsWith("banner")) return 16
            if (item.endsWith("_boat")) return 1
            if (item.endsWith("_bed")) return 1
            if (item.endsWith("shulker_box")) return 1
            if (item.endsWith("minecart")) return 1
            if (item.startsWith("music_disc")) return 1
            if (item.endsWith("banner_pattern")) return 1
            if (item.endsWith("potion")) return 1
            if (item.endsWith("horse_armor")) return 1
            if (item.endsWith("_bucket")) return 1
            if (item.endsWith("_pickaxe")) return 1
            if (item.endsWith("_axe")) return 1
            if (item.endsWith("_shovel")) return 1
            if (item.endsWith("_sword")) return 1
            if (item.endsWith("_hoe")) return 1
            if (item.endsWith("_helmet")) return 1
            if (item.endsWith("_chestplate")) return 1
            if (item.endsWith("_leggings")) return 1
            if (item.endsWith("_boots")) return 1
            return stackSizes.getOrElse(item) { 64 }
        }

    companion object : BookSerializable<SimpleMinecraftItem> {
        override val className = "SimpleMinecraftItem"

        override fun serializeToPages(it: SimpleMinecraftItem): List<String> {
            return listOf(it.id, it.amount.toString())
        }

        override fun deserializeFromPages(pages: List<String>): SimpleMinecraftItem {
            return SimpleMinecraftItem(pages[0], pages[1].toInt())
        }

        private val stackSizes = hashMapOf(
            "snowball" to 16,
            "armor_stand" to 16,
            "egg" to 16,
            "honey_bottle" to 16,
            "ender_pearl" to 16,
            "written_book" to 16,
            "bucket" to 16,
            "cake" to 1,
            "mushroom_stew" to 1,
            "suspicious_stew" to 1,
            "beetroot_soup" to 1,
            "rabbit_stew" to 1,
            "enchanted_book" to 1,
            "writable_book" to 1,
            "knowledge_book" to 1,
            "saddle" to 1,
            "flint_and_steel" to 1,
            "shears" to 1,
            "totem_of_undying" to 1,
            "fishing_rod" to 1,
            "carrot_on_a_stick" to 1,
            "warped_fungus_on_a_stick" to 1,
            "bow" to 1,
            "crossbow" to 1,
            "shield" to 1,
            "trident" to 1,
            "elytra" to 1,
            "debug_stick" to 1
        )
    }
}