package me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.backend.presets_examples.emptyItem
import me.repeater64.advancedmpkeditor.backend.presets_examples.optionList

open class ArmorSlotData(armorSlot: String, _itemOptions: WeightedOptionList<MinecraftItem>)
    : FixedSlotData("armor.$armorSlot", _itemOptions) {
}

@Stable
class HelmetSlotData(_itemOptions: WeightedOptionList<MinecraftItem>) : ArmorSlotData("head", _itemOptions) {
    override var itemOptions by mutableStateOf(_itemOptions)
    companion object : SingleSpecificFixedSlotCompanion<HelmetSlotData> {
        override val className = "HelmetSlotData"
        override fun empty() = HelmetSlotData(optionList(emptyItem()))
        override fun create(list: WeightedOptionList<MinecraftItem>) = HelmetSlotData(list)
    }
}
@Stable
class ChestplateSlotData(_itemOptions: WeightedOptionList<MinecraftItem>) : ArmorSlotData("chest", _itemOptions) {
    override var itemOptions by mutableStateOf(_itemOptions)
    companion object : SingleSpecificFixedSlotCompanion<ChestplateSlotData> {
        override val className = "ChestplateSlotData"
        override fun empty() = ChestplateSlotData(optionList(emptyItem()))
        override fun create(list: WeightedOptionList<MinecraftItem>) = ChestplateSlotData(list)
    }
}
@Stable
 class LeggingsSlotData(_itemOptions: WeightedOptionList<MinecraftItem>) : ArmorSlotData("legs", _itemOptions) {
    override var itemOptions by mutableStateOf(_itemOptions)
    companion object : SingleSpecificFixedSlotCompanion<LeggingsSlotData> {
        override val className = "LeggingsSlotData"
        override fun empty() = LeggingsSlotData(optionList(emptyItem()))
        override fun create(list: WeightedOptionList<MinecraftItem>) = LeggingsSlotData(list)
    }
}
@Stable
 class BootsSlotData(_itemOptions: WeightedOptionList<MinecraftItem>) : ArmorSlotData("feet", _itemOptions) {
    override var itemOptions by mutableStateOf(_itemOptions)
    companion object : SingleSpecificFixedSlotCompanion<BootsSlotData> {
        override val className = "BootsSlotData"
        override fun empty() = BootsSlotData(optionList(emptyItem()))
        override fun create(list: WeightedOptionList<MinecraftItem>) = BootsSlotData(list)
    }
}