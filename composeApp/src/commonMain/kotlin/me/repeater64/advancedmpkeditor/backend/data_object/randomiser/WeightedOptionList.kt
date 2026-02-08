package me.repeater64.advancedmpkeditor.backend.data_object.randomiser

import kotlin.math.pow

data class WeightedOptionList<T>(val options: MutableList<WeightedOption<T>>) {
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
}
