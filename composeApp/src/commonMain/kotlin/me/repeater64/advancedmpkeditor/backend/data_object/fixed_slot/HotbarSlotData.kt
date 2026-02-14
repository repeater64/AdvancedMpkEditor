package me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.backend.presets_examples.emptyItem
import me.repeater64.advancedmpkeditor.backend.presets_examples.hotbarSlot
import me.repeater64.advancedmpkeditor.backend.presets_examples.optionList
import me.repeater64.advancedmpkeditor.util.hash

@Stable
class HotbarSlotData(_hotbarPosition: Int, _itemOptions: WeightedOptionList<MinecraftItem>)
    : FixedSlotData("hotbar.$_hotbarPosition", _itemOptions) {
    val hotbarPosition by mutableStateOf(_hotbarPosition)
    override var itemOptions by mutableStateOf(_itemOptions)

    override fun contentHash(): Int {
        return hash(hotbarPosition, itemOptions.contentHash())
    }

    override val slotDisplayName = "Hotbar Slot ${hotbarPosition+1}"

    companion object : BookSerializable<HotbarSlotData> {
        override val className = "HotbarSlotData"

        override fun serializeToPages(it: HotbarSlotData): List<String> {
            return listOf(it.hotbarPosition.toString()) + WeightedOptionList.serialize(it.itemOptions as WeightedOptionList<Any>)
        }

        override fun deserializeFromPages(pages: List<String>): HotbarSlotData {
            return HotbarSlotData(pages[0].toInt(), BookSerializable.getObjectAndRemainingPages(pages.drop(1), WeightedOptionList).first as WeightedOptionList<MinecraftItem>)
        }

        fun empty(slotNum: Int) = hotbarSlot(slotNum, optionList(emptyItem()))
    }
}