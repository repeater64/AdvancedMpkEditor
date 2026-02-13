package me.repeater64.advancedmpkeditor.backend.data_object.fire_res

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import me.repeater64.advancedmpkeditor.backend.data_object.ContentHashable
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList

@Stable
class FireResSettings(
    _options: WeightedOptionList<Int> // Int is number of seconds of fire res
) : ContentHashable {
    val options by mutableStateOf(_options)

    override fun contentHash() = options.contentHash()

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