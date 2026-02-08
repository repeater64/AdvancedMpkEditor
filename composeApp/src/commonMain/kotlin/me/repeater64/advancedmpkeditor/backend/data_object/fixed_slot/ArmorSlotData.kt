package me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot

import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList

open class ArmorSlotData(armorSlot: String, override val itemOptions: WeightedOptionList<MinecraftItem>)
    : FixedSlotData("armor.$armorSlot", itemOptions) {
    override var forcedEmpty: Boolean
        get() = itemOptions.options.isEmpty()
        set(_) {}
}

data class HelmetSlotData(override val itemOptions: WeightedOptionList<MinecraftItem>) : ArmorSlotData("head", itemOptions)
data class ChestplateSlotData(override val itemOptions: WeightedOptionList<MinecraftItem>) : ArmorSlotData("chest", itemOptions)
data class LeggingsSlotData(override val itemOptions: WeightedOptionList<MinecraftItem>) : ArmorSlotData("legs", itemOptions)
data class BootsSlotData(override val itemOptions: WeightedOptionList<MinecraftItem>) : ArmorSlotData("feet", itemOptions)