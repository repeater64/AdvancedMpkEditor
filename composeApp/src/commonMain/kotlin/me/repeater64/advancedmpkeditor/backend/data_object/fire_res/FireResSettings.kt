package me.repeater64.advancedmpkeditor.backend.data_object.fire_res

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList

data class FireResSettings(
    val options: WeightedOptionList<Int> // Int is number of seconds of fire res
) {
    companion object : BookSerializable<FireResSettings> {
        override val className = "FireResSettings"

        override fun serializeToPages(it: FireResSettings): List<String> {
            return WeightedOptionList.serialize(it.options as WeightedOptionList<Any>)
        }

        override fun deserializeFromPages(pages: List<String>): FireResSettings {
            return FireResSettings(BookSerializable.getObjectAndRemainingPages(pages, WeightedOptionList).first as WeightedOptionList<Int>)
        }

    }
}