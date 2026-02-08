package me.repeater64.advancedmpkeditor.backend.data_object.randomiser

data class WeightedOption<T>(
    val option: T,
    val weight: Int = 1,
    val label: String? = null,
    val conditions: List<RandomiserCondition> = emptyList()
) : Comparable<WeightedOption<T>> {
    override fun compareTo(other: WeightedOption<T>): Int {
        return other.weight.compareTo(this.weight)
    }
}