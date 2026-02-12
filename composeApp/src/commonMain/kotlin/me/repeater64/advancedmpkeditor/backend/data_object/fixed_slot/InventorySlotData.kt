package me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.item.DontReplaceMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList

@Stable
class InventorySlotData(_inventoryPosition: Int, _itemOptions: WeightedOptionList<MinecraftItem>)
    : FixedSlotData("container.${_inventoryPosition+9}", _itemOptions) {
    val inventoryPosition by mutableStateOf(_inventoryPosition)
    override var itemOptions by mutableStateOf(_itemOptions)


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