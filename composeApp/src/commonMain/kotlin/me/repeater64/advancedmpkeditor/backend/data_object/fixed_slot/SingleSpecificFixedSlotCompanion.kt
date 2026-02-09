package me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList

interface SingleSpecificFixedSlotCompanion<T : FixedSlotData> : BookSerializable<T> {
    override fun serializeToPages(it: T): List<String> {
        return WeightedOptionList.serialize(it.itemOptions as WeightedOptionList<Any>)
    }

    override fun deserializeFromPages(pages: List<String>): T {
        return create(BookSerializable.getObjectAndRemainingPages(pages, WeightedOptionList).first as WeightedOptionList<MinecraftItem>)
    }

    fun empty(): T
    fun create(list: WeightedOptionList<MinecraftItem>): T
}