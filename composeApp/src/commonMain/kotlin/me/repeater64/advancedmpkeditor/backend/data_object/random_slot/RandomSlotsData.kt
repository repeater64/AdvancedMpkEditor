package me.repeater64.advancedmpkeditor.backend.data_object.random_slot

import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import me.repeater64.advancedmpkeditor.backend.data_object.ContentHashable
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.item.RandomBarterItem
import me.repeater64.advancedmpkeditor.util.hash

@Stable
class RandomSlotsData(_optionsSets: List<RandomSlotOptionsSet>) : ContentHashable {
    val optionsSets = _optionsSets.toMutableStateList()

    override fun contentHash() = hash(optionsSets.map { it.contentHash() })

    fun totalMaxNumStacks() = optionsSets.sumOf { it.maxNumStacks }

    companion object : BookSerializable<RandomSlotsData> {
        override val className = "RandomSlotsData"

        override fun serializeToPages(it: RandomSlotsData): List<String> {
            return BookSerializable.serializeList(it.optionsSets, RandomSlotOptionsSet)
        }

        override fun deserializeFromPages(pages: List<String>): RandomSlotsData {
            return RandomSlotsData(BookSerializable.getListAndRemainingPages(pages, RandomSlotOptionsSet).first)
        }

    }

    fun getReorderedOptionsSets(): List<RandomSlotOptionsSet> {
        // Reorder to put any sets containing "piglin barters" items at the end, as these are the ones that are most likely to underestimate
        // the number of inventory slots they will fill. Therefore doing them at the end minimises the damage done to slot randomisation.

        val setsContainingRandomBarters = mutableListOf<RandomSlotOptionsSet>()
        val otherSets = mutableListOf<RandomSlotOptionsSet>()

        for (set in optionsSets) {
            if (set.options.options.any { option -> option.option.any { it is RandomBarterItem }  }) {
                setsContainingRandomBarters.add(set)
            } else {
                otherSets.add(set)
            }
        }

        return otherSets + setsContainingRandomBarters
    }
}