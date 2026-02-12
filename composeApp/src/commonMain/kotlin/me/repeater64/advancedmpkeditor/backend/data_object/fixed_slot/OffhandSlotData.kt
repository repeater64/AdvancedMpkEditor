package me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot

import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.backend.presets_examples.emptyItem
import me.repeater64.advancedmpkeditor.backend.presets_examples.optionList

data class OffhandSlotData(override val itemOptions: WeightedOptionList<MinecraftItem>)
    : FixedSlotData("weapon.offhand", itemOptions) {

    companion object : SingleSpecificFixedSlotCompanion<OffhandSlotData> {
        override val className = "OffhandSlotData"
        override fun empty() = OffhandSlotData(optionList(emptyItem()))
        override fun create(list: WeightedOptionList<MinecraftItem>) = OffhandSlotData(list)
    }
}