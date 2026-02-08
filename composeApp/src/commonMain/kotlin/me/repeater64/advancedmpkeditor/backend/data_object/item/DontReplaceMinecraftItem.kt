package me.repeater64.advancedmpkeditor.backend.data_object.item

class DontReplaceMinecraftItem : MinecraftItem {
    override val amount: Int = 1
    override val commandString = "intentionally_invalid_command"
    override val displayName = "Available for Random Items"
    override val iconFile = "blank.png"
}