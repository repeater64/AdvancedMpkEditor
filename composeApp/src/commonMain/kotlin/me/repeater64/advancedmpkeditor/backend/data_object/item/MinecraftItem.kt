package me.repeater64.advancedmpkeditor.backend.data_object.item

interface MinecraftItem {
    val commandString: String
    val displayName: String
    val amount: Int
    val iconFile: String
}