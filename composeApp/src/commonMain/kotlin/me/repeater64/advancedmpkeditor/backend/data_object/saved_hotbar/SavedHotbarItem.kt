package me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar

import net.benwoodworth.knbt.NbtCompound
import net.benwoodworth.knbt.NbtString

abstract class SavedHotbarItem {
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