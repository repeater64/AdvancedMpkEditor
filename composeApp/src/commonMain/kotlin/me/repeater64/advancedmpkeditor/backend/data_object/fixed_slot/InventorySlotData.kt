package me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot

import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.backend.emptyItem
import me.repeater64.advancedmpkeditor.backend.hotbarSlot
import me.repeater64.advancedmpkeditor.backend.invSlot
import me.repeater64.advancedmpkeditor.backend.optionList

data class InventorySlotData(val inventoryPosition: Int, override val itemOptions: WeightedOptionList<MinecraftItem>)
    : FixedSlotData("container.${inventoryPosition+9}", itemOptions) {

    companion object : FixedSlotDataFactory<InventorySlotData> {
        override val slotStringPatternToLookFor = "container"
        override fun create(slotString: String) = InventorySlotData(slotString.removePrefix("container.").toInt()-9, WeightedOptionList(mutableListOf()))
        override fun empty(slotNum: Int) = invSlot(slotNum, optionList(emptyItem()))
    }
}