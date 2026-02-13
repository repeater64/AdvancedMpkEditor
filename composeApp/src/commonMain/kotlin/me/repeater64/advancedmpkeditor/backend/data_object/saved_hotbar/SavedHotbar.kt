package me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar

import androidx.compose.runtime.Stable
import androidx.compose.runtime.toMutableStateList
import me.repeater64.advancedmpkeditor.backend.data_object.ContentHashable
import me.repeater64.advancedmpkeditor.util.hash
import net.benwoodworth.knbt.NbtCompound
import net.benwoodworth.knbt.NbtList
import net.benwoodworth.knbt.add
import net.benwoodworth.knbt.buildNbtList

@Stable
class SavedHotbar(
    _hotbarItems: List<SavedHotbarItem>
): ContentHashable {
    val hotbarItems = _hotbarItems.toMutableStateList()

    override fun contentHash(): Int {
        return hash(hotbarItems.map { it.contentHash() })
    }

    fun getTag(): NbtList<NbtCompound> {
        return buildNbtList {
            for (hotbarItem in hotbarItems) {
                add(hotbarItem.getTag())
            }
        }
    }

    companion object {
        fun fromTag(tag: NbtList<NbtCompound>) : SavedHotbar {
            val hotbarItems = List(tag.size) {SavedHotbarItem.fromTag(tag[it])}
            return SavedHotbar(hotbarItems)
        }

        fun emptyHotbar() : SavedHotbar {
            return SavedHotbar(List(9) { AirItem() })
        }
    }
}