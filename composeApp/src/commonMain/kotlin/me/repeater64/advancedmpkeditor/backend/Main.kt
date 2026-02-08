package me.repeater64.advancedmpkeditor.backend

import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.RandomiserCondition
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbars
import okio.FileSystem
import okio.Path.Companion.toPath


fun main() {
    println("hi")

    val list = WeightedOptionList(
        mutableListOf(
            WeightedOption("opt1", 1, null, emptyList()),
            WeightedOption("opt2", 1, null, emptyList()),
            WeightedOption("opt3", 1, null, listOf(RandomiserCondition("cond1", false))),
            WeightedOption("opt4", 1, null, listOf(RandomiserCondition("cond1", false), RandomiserCondition("cond2", true))),
            WeightedOption("opt5", 1, null, listOf(RandomiserCondition("cond1", true))),
            WeightedOption("opt6", 1, null, listOf(RandomiserCondition("cond1", true), RandomiserCondition("cond2", false))),
        )
    )

    val all = list.generateAllConditionLists()
    for ((key, value) in all) {
        println("$key: ${value.options.map { it.option }}")
    }
}
