package me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot

import androidx.compose.runtime.toMutableStateList
import me.repeater64.advancedmpkeditor.backend.data_object.ContentHashable
import me.repeater64.advancedmpkeditor.backend.data_object.CopyPasteable
import me.repeater64.advancedmpkeditor.backend.data_object.item.DontReplaceMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.ForcedEmptyMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.random_slot.RandomSlotOptionsSet
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList

abstract class FixedSlotData(val minecraftSlotID: String, open var itemOptions: WeightedOptionList<MinecraftItem>) : ContentHashable, CopyPasteable<Any> {
    abstract val slotDisplayName: String
    override fun contentHash() = itemOptions.contentHash()

    override fun copyInto(other: Any) {
        if (other is FixedSlotData) {
            if (other.itemOptions.options.size == 1 && (other.itemOptions.options[0].option is DontReplaceMinecraftItem || other.itemOptions.options[0].option is ForcedEmptyMinecraftItem)) {
                other.itemOptions.options.clear()
            }

            other.itemOptions.options.addAll(this.itemOptions.options.map { it.deepCopy() })
        } else if (other is RandomSlotOptionsSet) {
            if (other.options.options.size == 1 && (other.options.options[0].option.size == 1 && other.options.options[0].option[0] is DontReplaceMinecraftItem)) {
                other.options.options.clear()
            }

            other.options.options.addAll(this.itemOptions.options.map { it.deepCopy() }.map { WeightedOption(listOf(it.option).toMutableStateList(), it.weight, it.label, it.conditions) })
        }
    }

    override fun typeMatches(other: Any): Boolean {
        return other is FixedSlotData || other is RandomSlotOptionsSet
    }
}