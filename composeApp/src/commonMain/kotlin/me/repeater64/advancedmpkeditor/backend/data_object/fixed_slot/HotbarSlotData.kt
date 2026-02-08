package me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot

import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList

data class HotbarSlotData(val hotbarPosition: Int, override val itemOptions: WeightedOptionList<MinecraftItem>)
    : FixedSlotData("hotbar.$hotbarPosition", itemOptions) {
    override var forcedEmpty: Boolean
        get() = itemOptions.options.isEmpty()
        set(_) {}
}