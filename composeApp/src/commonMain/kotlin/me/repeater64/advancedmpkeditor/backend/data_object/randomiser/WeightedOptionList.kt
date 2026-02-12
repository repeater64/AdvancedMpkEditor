package me.repeater64.advancedmpkeditor.backend.data_object.randomiser

import androidx.compose.runtime.Stable
import androidx.compose.runtime.toMutableStateList
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import kotlin.math.pow

@Stable
class WeightedOptionList<T>(_options: MutableList<WeightedOption<T>>) {
    val options = _options.toMutableStateList()
    val optionsSortedByWeight = options.sorted()

    fun generateAllConditionLists(): Map<Set<RandomiserCondition>, UnconditionalWeightedList<T>> {
        val toReturn: MutableMap<Set<RandomiserCondition>, UnconditionalWeightedList<T>> = HashMap()
        val allConditionLabels = options.flatMap { it.conditions.map { c -> c.conditionLabel } }.distinct()

        val numCombinations = 2.0.pow(allConditionLabels.size).toInt()

        for (i in 0 until numCombinations) {
            val currentCombination = mutableSetOf<RandomiserCondition>()
            for (j in allConditionLabels.indices) {
                val isInverted = (i shr j) and 1 == 1
                currentCombination.add(RandomiserCondition(allConditionLabels[j], isInverted))
            }

            val validOptions = options.filter { option ->
                for (condition in currentCombination) {
                    if (!condition.isInverted) {
                        // This condition is a requirement, the option must also require this condition
                        // Check the option contains the affirmative condition
                        if (!option.conditions.contains(condition)) {
                            return@filter false
                        }
                    } else {
                        // This condition is an exclusion, the option must not require this condition
                        // Check the option doesn't require the affirmative condition
                        if (option.conditions.contains(RandomiserCondition(condition.conditionLabel, false))) {
                            return@filter false
                        }
                    }
                }
                return@filter true
            }

            if (validOptions.isNotEmpty()) {
                toReturn[currentCombination] = UnconditionalWeightedList(validOptions.toMutableList())
            }
        }

        return toReturn
    }

    companion object  : BookSerializable<WeightedOptionList<Any>> {
        override val className = "WeightedOptionList"

        override fun serializeToPages(it: WeightedOptionList<Any>): List<String> {
            return BookSerializable.serializeList(it.options, WeightedOption)
        }

        override fun deserializeFromPages(pages: List<String>): WeightedOptionList<Any> {
            val list = BookSerializable.getListAndRemainingPages(pages, WeightedOption).first
            return WeightedOptionList(list.toMutableList())
        }
    }

    fun <P: Comparable<P>> getMaximumPossibleProperty(propertyGetter: (T) -> P): P {
        return options.maxOf { propertyGetter(it.option) }
    }

    fun <P: Comparable<P>> getMinimumPossibleProperty(propertyGetter: (T) -> P): P {
        return options.minOf { propertyGetter(it.option) }
    }
}
