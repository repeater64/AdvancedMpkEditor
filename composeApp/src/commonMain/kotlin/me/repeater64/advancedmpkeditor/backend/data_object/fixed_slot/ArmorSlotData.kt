package me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot

import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.backend.emptyItem
import me.repeater64.advancedmpkeditor.backend.optionList

open class ArmorSlotData(armorSlot: String, override val itemOptions: WeightedOptionList<MinecraftItem>)
    : FixedSlotData("armor.$armorSlot", itemOptions) {
}

data class HelmetSlotData(override val itemOptions: WeightedOptionList<MinecraftItem>) : ArmorSlotData("head", itemOptions) {
    companion object : FixedSlotDataFactory<HelmetSlotData> {
        override val slotStringPatternToLookFor = "armor.head"
        override fun create(slotString: String) = HelmetSlotData(WeightedOptionList(mutableListOf()))
        override fun empty(slotNum: Int) = HelmetSlotData(optionList(emptyItem()))
    }
}
data class ChestplateSlotData(override val itemOptions: WeightedOptionList<MinecraftItem>) : ArmorSlotData("chest", itemOptions) {
    companion object : FixedSlotDataFactory<ChestplateSlotData> {
        override val slotStringPatternToLookFor = "armor.chest"
        override fun create(slotString: String) = ChestplateSlotData(WeightedOptionList(mutableListOf()))
        override fun empty(slotNum: Int) = ChestplateSlotData(optionList(emptyItem()))
    }
}
data class LeggingsSlotData(override val itemOptions: WeightedOptionList<MinecraftItem>) : ArmorSlotData("legs", itemOptions) {
    companion object : FixedSlotDataFactory<LeggingsSlotData> {
        override val slotStringPatternToLookFor = "armor.legs"
        override fun create(slotString: String) = LeggingsSlotData(WeightedOptionList(mutableListOf()))
        override fun empty(slotNum: Int) = LeggingsSlotData(optionList(emptyItem()))
    }
}
data class BootsSlotData(override val itemOptions: WeightedOptionList<MinecraftItem>) : ArmorSlotData("feet", itemOptions) {
    companion object : FixedSlotDataFactory<BootsSlotData> {
        override val slotStringPatternToLookFor = "armor.feet"
        override fun create(slotString: String) = BootsSlotData(WeightedOptionList(mutableListOf()))
        override fun empty(slotNum: Int) = BootsSlotData(optionList(emptyItem()))
    }
}