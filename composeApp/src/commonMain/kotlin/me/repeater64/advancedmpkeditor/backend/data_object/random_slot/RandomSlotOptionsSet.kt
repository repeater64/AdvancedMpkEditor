package me.repeater64.advancedmpkeditor.backend.data_object.random_slot

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import me.repeater64.advancedmpkeditor.backend.data_object.ContentHashable
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.util.hash

@Stable
class RandomSlotOptionsSet(_setName: String, _options: WeightedOptionList<SnapshotStateList<MinecraftItem>>) : ContentHashable {
    var setName by mutableStateOf(_setName)
    val options by mutableStateOf(_options)

    override fun contentHash() = hash(setName, options.contentHash())

    val numStacksProperty: (List<MinecraftItem>) -> Int = { it.fold(0) { acc, item -> acc + item.numStacks } }
    val maxNumStacks = options.getMaximumPossibleProperty(numStacksProperty)
    val minNumStacks = options.getMinimumPossibleProperty(numStacksProperty)

    companion object : BookSerializable<RandomSlotOptionsSet> {
        override val className = "RandomSlotOptionsSet"

        override fun serializeToPages(it: RandomSlotOptionsSet): List<String> {
            return listOf(it.setName) + WeightedOptionList.serialize(it.options as WeightedOptionList<Any>)
        }

        override fun deserializeFromPages(pages: List<String>): RandomSlotOptionsSet {
            return RandomSlotOptionsSet(pages[0], BookSerializable.getObjectAndRemainingPages(pages.drop(1), WeightedOptionList).first as WeightedOptionList<SnapshotStateList<MinecraftItem>>)
        }
    }
}