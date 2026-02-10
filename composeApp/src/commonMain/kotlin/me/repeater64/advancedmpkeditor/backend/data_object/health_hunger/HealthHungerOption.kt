package me.repeater64.advancedmpkeditor.backend.data_object.health_hunger

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable

data class HealthHungerOption(
    val healthOption: HealthOption,
    val hungerOption: HungerOption
) {
    companion object : BookSerializable<HealthHungerOption> {
        override val className = "HealthHungerOption"

        override fun serializeToPages(it: HealthHungerOption): List<String> {
            return listOf(it.healthOption.name, it.hungerOption.name)
        }

        override fun deserializeFromPages(pages: List<String>): HealthHungerOption {
            return HealthHungerOption(HealthOption.valueOf(pages[0]), HungerOption.valueOf(pages[1]))
        }
    }
}