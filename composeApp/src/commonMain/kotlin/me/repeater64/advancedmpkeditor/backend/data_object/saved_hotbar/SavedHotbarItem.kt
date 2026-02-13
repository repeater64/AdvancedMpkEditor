package me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar

import me.repeater64.advancedmpkeditor.backend.data_object.ContentHashable
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import net.benwoodworth.knbt.NbtCompound
import net.benwoodworth.knbt.NbtString

abstract class SavedHotbarItem: ContentHashable {
    abstract fun getGuiRepresentationItem(): MinecraftItem
    abstract fun getGuiName(): String
    abstract fun getTag(): NbtCompound

    companion object {
        fun fromTag(tag: NbtCompound): SavedHotbarItem {
            return when ((tag["id"] as NbtString).value) {
                "minecraft:barrel" -> BarrelItem.fromTag(tag)
                "minecraft:repeating_command_block" -> CommandBlockItem()
                else -> AirItem()
            }
        }
    }
}