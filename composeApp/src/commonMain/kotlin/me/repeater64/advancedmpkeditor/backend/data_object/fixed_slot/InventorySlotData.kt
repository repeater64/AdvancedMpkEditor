package me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.item.DontReplaceMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.backend.emptyItem
import me.repeater64.advancedmpkeditor.backend.hotbarSlot
import me.repeater64.advancedmpkeditor.backend.invSlot
import me.repeater64.advancedmpkeditor.backend.optionList

data class InventorySlotData(val inventoryPosition: Int, override val itemOptions: WeightedOptionList<MinecraftItem>)
    : FixedSlotData("container.${inventoryPosition+9}", itemOptions) {

    fun isAvailableForRandomItems(): Boolean = itemOptions.options.size == 1 && itemOptions.options[0].option is DontReplaceMinecraftItem

    companion object : BookSerializable<InventorySlotData> {
        override val className = "InventorySlotData"

        override fun serializeToPages(it: InventorySlotData): List<String> {
            return listOf(it.inventoryPosition.toString()) + WeightedOptionList.serialize(it.itemOptions as WeightedOptionList<Any>)
        }

        override fun deserializeFromPages(pages: List<String>): InventorySlotData {
            return InventorySlotData(pages[0].toInt(), BookSerializable.getObjectAndRemainingPages(pages.drop(1), WeightedOptionList).first as WeightedOptionList<MinecraftItem>)
        }
    }
}