package me.repeater64.advancedmpkeditor.backend.data_object

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.FixedSlotsData
import me.repeater64.advancedmpkeditor.backend.data_object.random_slot.RandomSlotsData
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList

data class AllCommandsSettings(
    val fixedSlotsData: FixedSlotsData,
    val randomSlotsData: RandomSlotsData
) {
    companion object : BookSerializable<AllCommandsSettings> {
        override val className = "AllCommandsSettings"

        override fun serializeToPages(it: AllCommandsSettings): List<String> {
            return FixedSlotsData.serialize(it.fixedSlotsData) + RandomSlotsData.serialize(it.randomSlotsData)
        }

        override fun deserializeFromPages(pages: List<String>): AllCommandsSettings {
            val (fixedSlotsData, remainingPages) = BookSerializable.getObjectAndRemainingPages(pages, FixedSlotsData)
            val randomSlotsData = BookSerializable.getObjectAndRemainingPages(remainingPages, RandomSlotsData).first
            return AllCommandsSettings(fixedSlotsData, randomSlotsData)
        }

    }
}