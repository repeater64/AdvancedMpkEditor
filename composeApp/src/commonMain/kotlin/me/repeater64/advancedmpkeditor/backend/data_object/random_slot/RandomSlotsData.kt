package me.repeater64.advancedmpkeditor.backend.data_object.random_slot

import androidx.compose.runtime.Stable
import androidx.compose.runtime.toMutableStateList
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable

@Stable
class RandomSlotsData(_optionsSets: List<RandomSlotOptionsSet>) {
    val optionsSets = _optionsSets.toMutableStateList()

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
}