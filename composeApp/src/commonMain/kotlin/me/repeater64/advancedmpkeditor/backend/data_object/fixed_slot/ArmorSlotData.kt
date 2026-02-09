package me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot

import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.backend.emptyItem
import me.repeater64.advancedmpkeditor.backend.optionList

open class ArmorSlotData(armorSlot: String, override val itemOptions: WeightedOptionList<MinecraftItem>)
    : FixedSlotData("armor.$armorSlot", itemOptions) {
}

data class HelmetSlotData(override val itemOptions: WeightedOptionList<MinecraftItem>) : ArmorSlotData("head", itemOptions) {
    companion object : SingleSpecificFixedSlotCompanion<HelmetSlotData> {
        override val className = "HelmetSlotData"
        override fun empty() = HelmetSlotData(optionList(emptyItem()))
        override fun create(list: WeightedOptionList<MinecraftItem>) = HelmetSlotData(list)
    }
}
data class ChestplateSlotData(override val itemOptions: WeightedOptionList<MinecraftItem>) : ArmorSlotData("chest", itemOptions) {
    companion object : SingleSpecificFixedSlotCompanion<ChestplateSlotData> {
        override val className = "ChestplateSlotData"
        override fun empty() = ChestplateSlotData(optionList(emptyItem()))
        override fun create(list: WeightedOptionList<MinecraftItem>) = ChestplateSlotData(list)
    }
}
data class LeggingsSlotData(override val itemOptions: WeightedOptionList<MinecraftItem>) : ArmorSlotData("legs", itemOptions) {
    companion object : SingleSpecificFixedSlotCompanion<LeggingsSlotData> {
        override val className = "LeggingsSlotData"
        override fun empty() = LeggingsSlotData(optionList(emptyItem()))
        override fun create(list: WeightedOptionList<MinecraftItem>) = LeggingsSlotData(list)
    }
}
data class BootsSlotData(override val itemOptions: WeightedOptionList<MinecraftItem>) : ArmorSlotData("feet", itemOptions) {
    companion object : SingleSpecificFixedSlotCompanion<BootsSlotData> {
        override val className = "BootsSlotData"
        override fun empty() = BootsSlotData(optionList(emptyItem()))
        override fun create(list: WeightedOptionList<MinecraftItem>) = BootsSlotData(list)
    }
}