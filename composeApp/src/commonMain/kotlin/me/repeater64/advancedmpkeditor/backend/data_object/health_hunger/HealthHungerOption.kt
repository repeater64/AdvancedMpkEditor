package me.repeater64.advancedmpkeditor.backend.data_object.health_hunger

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import me.repeater64.advancedmpkeditor.backend.data_object.ContentHashable
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.util.hash

@Stable
class HealthHungerOption(
    _healthOption: HealthOption,
    _hungerOption: HungerOption
) : ContentHashable {
    var healthOption by mutableStateOf(_healthOption)
    var hungerOption by mutableStateOf(_hungerOption)

    override fun contentHash(): Int {
        return hash(healthOption, hungerOption)
    }

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