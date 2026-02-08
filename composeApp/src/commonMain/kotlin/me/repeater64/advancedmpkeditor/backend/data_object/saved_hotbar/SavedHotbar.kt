package me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar

import net.benwoodworth.knbt.NbtCompound
import net.benwoodworth.knbt.NbtList
import net.benwoodworth.knbt.add
import net.benwoodworth.knbt.buildNbtList

data class SavedHotbar(
    val hotbarItems: Array<SavedHotbarItem>
) {
    fun getTag(): NbtList<NbtCompound> {
        return buildNbtList {
            for (hotbarItem in hotbarItems) {
                add(hotbarItem.getTag())
            }
        }
    }

    companion object {
        fun fromTag(tag: NbtList<NbtCompound>) : SavedHotbar {
            val hotbarItems = Array(tag.size) {SavedHotbarItem.fromTag(tag[it])}
            return SavedHotbar(hotbarItems)
        }

        fun emptyHotbar() : SavedHotbar {
            return SavedHotbar(Array(9) { AirItem() })
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as SavedHotbar

        if (!hotbarItems.contentEquals(other.hotbarItems)) return false

        return true
    }

    override fun hashCode(): Int {
        return hotbarItems.contentHashCode()
    }
}