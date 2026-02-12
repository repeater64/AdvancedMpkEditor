package me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.backend.presets_examples.emptyItem
import me.repeater64.advancedmpkeditor.backend.presets_examples.optionList

@Stable
class OffhandSlotData(_itemOptions: WeightedOptionList<MinecraftItem>)
    : FixedSlotData("weapon.offhand", _itemOptions) {

    override var itemOptions by mutableStateOf(_itemOptions)

    companion object : SingleSpecificFixedSlotCompanion<OffhandSlotData> {
        override val className = "OffhandSlotData"
        override fun empty() = OffhandSlotData(optionList(emptyItem()))
        override fun create(list: WeightedOptionList<MinecraftItem>) = OffhandSlotData(list)
    }
}