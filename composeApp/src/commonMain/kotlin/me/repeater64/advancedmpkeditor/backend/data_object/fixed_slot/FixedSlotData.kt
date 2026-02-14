package me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot

import me.repeater64.advancedmpkeditor.backend.data_object.ContentHashable
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList

abstract class FixedSlotData(val minecraftSlotID: String, open var itemOptions: WeightedOptionList<MinecraftItem>) : ContentHashable {
    abstract val slotDisplayName: String
    override fun contentHash() = itemOptions.contentHash()
}