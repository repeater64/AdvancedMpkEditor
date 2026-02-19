package me.repeater64.advancedmpkeditor.backend.data_object.item

interface MinecraftItemWithAmount : MinecraftItem {
    fun copyWithAmount(amount: Int): MinecraftItemWithAmount
}