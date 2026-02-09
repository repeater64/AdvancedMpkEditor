package me.repeater64.advancedmpkeditor.backend.data_object.randomiser

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable

data class RandomiserCondition(
    val conditionLabel: String,
    val isInverted: Boolean = false,
) {
    companion object : BookSerializable<RandomiserCondition> {
        override val className = "RandomiserCondition"

        override fun serializeToPages(it: RandomiserCondition): List<String> {
            return listOf(it.conditionLabel, it.isInverted.toString())
        }

        override fun deserializeFromPages(pages: List<String>): RandomiserCondition {
            return RandomiserCondition(pages[0], pages[1].toBoolean())
        }

    }
}