package me.repeater64.advancedmpkeditor.backend.data_object.randomiser

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem

data class WeightedOption<T>(
    val option: T,
    val weight: Int = 1,
    val label: String? = null,
    val conditions: List<RandomiserCondition> = emptyList()
) : Comparable<WeightedOption<T>> {
    override fun compareTo(other: WeightedOption<T>): Int {
        return other.weight.compareTo(this.weight)
    }

    companion object : BookSerializable<WeightedOption<Any>> {
        override val className: String = "WeightedOption"

        override fun serializeToPages(it: WeightedOption<Any>): List<String> {
            val pages = mutableListOf<String>()
            pages.add(it.weight.toString())
            pages.add(it.label.toString())
            pages.addAll(BookSerializable.serializeList(it.conditions, RandomiserCondition))

            if (it.option is MinecraftItem) {
                pages.add("!MinecraftItem")

                pages.addAll(MinecraftItem.serialize(it.option))
            } else if (it.option is List<*>) {
                pages.add("!MinecraftItemList")
                if (it.option.isEmpty()) {
                    throw IllegalArgumentException("Can't serialize a Weighted Option containing an empty list!")
                }
                if (it.option.first() !is MinecraftItem) {
                    throw NotImplementedError("Book Serialization has not been implemented for WeightedOptions of type other than MinecraftItem and List<MinecraftItem>")
                }

                val minecraftItemList = it.option as List<MinecraftItem>
                pages.addAll(BookSerializable.serializeList(minecraftItemList, MinecraftItem))
            } else {
                throw NotImplementedError("Book Serialization has not been implemented for WeightedOptions of type other than MinecraftItem and List<MinecraftItem>")
            }

            return pages
        }

        override fun deserializeFromPages(pages: List<String>): WeightedOption<Any> {
            val weight = pages[0].toInt()
            val label = if (pages[1] == "null") null else pages[1]
            val (conditions, remainingPages) = BookSerializable.getListAndRemainingPages(pages.drop(2), RandomiserCondition)

            val option = if (remainingPages[0] == "!MinecraftItem") {
                BookSerializable.getObjectAndRemainingPages(remainingPages.drop(1), MinecraftItem).first
            } else if (remainingPages[0] == "!MinecraftItemList") {
                BookSerializable.getListAndRemainingPages(remainingPages.drop(1), MinecraftItem).first
            } else {
                throw IllegalArgumentException("Expected !MinecraftItem or !MinecraftItemList whilst deserializing WeightedOption but found ${remainingPages[0]}")
            }

            return WeightedOption(option, weight, label, conditions)
        }
    }
}