package me.repeater64.advancedmpkeditor.backend.data_object.item

data class SimpleMinecraftItem(private val id: String, override val amount: Int) : MinecraftItem {
    override val commandString = "$id $amount"
    override val displayName = id.split("_").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
    override val iconFile = "${id}.png"
}