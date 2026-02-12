package me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.backend.presets_examples.emptyItem
import me.repeater64.advancedmpkeditor.backend.presets_examples.hotbarSlot
import me.repeater64.advancedmpkeditor.backend.presets_examples.optionList

data class HotbarSlotData(val hotbarPosition: Int, override val itemOptions: WeightedOptionList<MinecraftItem>)
    : FixedSlotData("hotbar.$hotbarPosition", itemOptions) {

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