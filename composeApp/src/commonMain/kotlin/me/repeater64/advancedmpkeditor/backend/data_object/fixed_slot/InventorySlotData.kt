package me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot

import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList

data class InventorySlotData(val inventoryPosition: Int, override var forcedEmpty: Boolean, override val itemOptions: WeightedOptionList<MinecraftItem>)
    : FixedSlotData("container.${inventoryPosition+9}", itemOptions) {
}