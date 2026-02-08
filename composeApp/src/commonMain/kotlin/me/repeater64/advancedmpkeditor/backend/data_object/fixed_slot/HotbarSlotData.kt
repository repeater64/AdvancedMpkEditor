package me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot

import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.backend.emptyItem
import me.repeater64.advancedmpkeditor.backend.hotbarSlot
import me.repeater64.advancedmpkeditor.backend.optionList

data class HotbarSlotData(val hotbarPosition: Int, override val itemOptions: WeightedOptionList<MinecraftItem>)
    : FixedSlotData("hotbar.$hotbarPosition", itemOptions) {

    companion object : FixedSlotDataFactory<HotbarSlotData> {
        override val slotStringPatternToLookFor = "hotbar"
        override fun create(slotString: String) = HotbarSlotData(slotString.removePrefix("hotbar.").toInt(), WeightedOptionList(mutableListOf()))
        override fun empty(slotNum: Int) = hotbarSlot(slotNum, optionList(emptyItem()))
    }
}