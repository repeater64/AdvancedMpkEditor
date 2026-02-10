package me.repeater64.advancedmpkeditor.backend.data_object.health_hunger

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList

data class HealthHungerSettings(
    val options: WeightedOptionList<HealthHungerOption>
) {
    companion object : BookSerializable<HealthHungerSettings> {
        override val className = "HealthHungerSettings"

        override fun serializeToPages(it: HealthHungerSettings): List<String> {
            return WeightedOptionList.serialize(it.options as WeightedOptionList<Any>)
        }

        override fun deserializeFromPages(pages: List<String>): HealthHungerSettings {
            return HealthHungerSettings(BookSerializable.getObjectAndRemainingPages(pages, WeightedOptionList).first as WeightedOptionList<HealthHungerOption>)
        }

    }
}