package me.repeater64.advancedmpkeditor.backend.data_object.random_slot

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable

data class RandomSlotsData(val optionsSets: List<RandomSlotOptionsSet>) {

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