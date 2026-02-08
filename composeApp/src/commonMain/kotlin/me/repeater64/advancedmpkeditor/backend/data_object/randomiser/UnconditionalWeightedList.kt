package me.repeater64.advancedmpkeditor.backend.data_object.randomiser

data class UnconditionalWeightedList<T>(val options: MutableList<WeightedOption<T>>) {
    val totalWeight = options.sumOf { it.weight }
}