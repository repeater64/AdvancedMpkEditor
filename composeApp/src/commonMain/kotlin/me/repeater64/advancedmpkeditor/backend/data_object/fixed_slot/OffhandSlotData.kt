package me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot

import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.backend.emptyItem
import me.repeater64.advancedmpkeditor.backend.hotbarSlot
import me.repeater64.advancedmpkeditor.backend.optionList

data class OffhandSlotData(override val itemOptions: WeightedOptionList<MinecraftItem>)
    : FixedSlotData("weapon.offhand", itemOptions) {

    companion object : FixedSlotDataFactory<OffhandSlotData> {
        override val slotStringPatternToLookFor = "weapon.offhand"
        override fun create(slotString: String) = OffhandSlotData(WeightedOptionList(mutableListOf()))
        override fun empty(slotNum: Int) = OffhandSlotData(optionList(emptyItem()))
    }
}