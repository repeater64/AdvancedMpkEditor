package me.repeater64.advancedmpkeditor.backend.data_object.random_slot

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList

data class RandomSlotOptionsSet(val setName: String, val options: WeightedOptionList<List<MinecraftItem>>) {
    val numStacksProperty: (List<MinecraftItem>) -> Int = { it.fold(0) { acc, item -> acc + item.numStacks } }
    val maxNumStacks = options.getMaximumPossibleProperty(numStacksProperty)
    val minNumStacks = options.getMinimumPossibleProperty(numStacksProperty)

    companion object : BookSerializable<RandomSlotOptionsSet> {
        override val className = "RandomSlotOptionsSet"

        override fun serializeToPages(it: RandomSlotOptionsSet): List<String> {
            return listOf(it.setName) + WeightedOptionList.serialize(it.options as WeightedOptionList<Any>)
        }

        override fun deserializeFromPages(pages: List<String>): RandomSlotOptionsSet {
            return RandomSlotOptionsSet(pages[0], BookSerializable.getObjectAndRemainingPages(pages.drop(1), WeightedOptionList).first as WeightedOptionList<List<MinecraftItem>>)
        }
    }
}