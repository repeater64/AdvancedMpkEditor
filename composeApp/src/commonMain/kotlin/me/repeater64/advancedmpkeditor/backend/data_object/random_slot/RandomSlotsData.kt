package me.repeater64.advancedmpkeditor.backend.data_object.random_slot

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import me.repeater64.advancedmpkeditor.backend.data_object.ContentHashable
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.item.RandomBarterItem
import me.repeater64.advancedmpkeditor.util.hash

@Stable
class RandomSlotsData(_announceExplosives: Boolean, _optionsSets: List<RandomSlotOptionsSet>) : ContentHashable {
    val optionsSets = _optionsSets.toMutableStateList()
    var announceExplosives by mutableStateOf(_announceExplosives)

    override fun contentHash() = hash(optionsSets.map { it.contentHash() })

    fun totalMaxNumStacks() = optionsSets.sumOf { it.maxNumStacks }

    companion object : BookSerializable<RandomSlotsData> {
        override val className = "RandomSlotsData"

        override fun serializeToPages(it: RandomSlotsData): List<String> {
            return listOf(it.announceExplosives.toString()) + BookSerializable.serializeList(it.optionsSets, RandomSlotOptionsSet)
        }

        override fun deserializeFromPages(pages: List<String>): RandomSlotsData {
            // A little bit of backwards compatibility since I made this change since semi-releasing the website
            if (pages.size != 0 && pages[0].toBooleanStrictOrNull() != null) {
                return RandomSlotsData(pages[0].toBoolean(), BookSerializable.getListAndRemainingPages(pages.drop(1), RandomSlotOptionsSet).first)
            } else {
                return RandomSlotsData(false, BookSerializable.getListAndRemainingPages(pages, RandomSlotOptionsSet).first)
            }
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