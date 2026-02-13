package me.repeater64.advancedmpkeditor.backend.data_object.randomiser

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import me.repeater64.advancedmpkeditor.backend.data_object.ContentHashable
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HealthHungerOption
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.util.hash

@Stable
class WeightedOption<T>(
    _option: T,
    _weight: Int = 1,
    _label: String? = null,
    _conditions: List<RandomiserCondition> = emptyList()
) : Comparable<WeightedOption<T>>, ContentHashable {
    var option by mutableStateOf(_option)
    var weight by mutableStateOf(_weight)
    var label by mutableStateOf(_label)
    val conditions = _conditions.toMutableStateList()

    override fun contentHash(): Int {
        val optionHash = if (option is ContentHashable) {
            (option as ContentHashable).contentHash()
        } else if (option is Collection<*>) {
            (option as Iterable<Any?>).map { if (it is ContentHashable) it.contentHash() else it.hashCode() }
        } else {
            option.hashCode()
        }
        return hash(
            optionHash,
            weight,
            label,
            conditions.map { it.hashCode() } // Can use normal hashCode for RandomiserConditions as they are true immutable data classes
        )
    }

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

                pages.addAll(MinecraftItem.serialize(it.option as MinecraftItem))
            } else if (it.option is HealthHungerOption) {
                pages.add("!HealthHungerOption")
                pages.addAll(HealthHungerOption.serialize(it.option as HealthHungerOption))
            } else if (it.option is Int) {
                pages.add("!Int")
                pages.add(it.option.toString())
            } else if (it.option is List<*>) {
                pages.add("!MinecraftItemList")
                if ((it.option as List<Any?>).isEmpty()) {
                    throw IllegalArgumentException("Can't serialize a Weighted Option containing an empty list!")
                }
                if ((it.option as List<Any?>).first() !is MinecraftItem) {
                    throw NotImplementedError("Book Serialization has not been implemented for WeightedOptions of type other than MinecraftItem, List<MinecraftItem>, Int and HealthHungerOption")
                }

                val minecraftItemList = it.option as List<MinecraftItem>
                pages.addAll(BookSerializable.serializeList(minecraftItemList, MinecraftItem))
            } else {
                throw NotImplementedError("Book Serialization has not been implemented for WeightedOptions of type other than MinecraftItem, List<MinecraftItem>, Int and HealthHungerOption")
            }

            return pages
        }

        override fun deserializeFromPages(pages: List<String>): WeightedOption<Any> {
            val weight = pages[0].toInt()
            val label = if (pages[1] == "null") null else pages[1]
            val (conditions, remainingPages) = BookSerializable.getListAndRemainingPages(pages.drop(2), RandomiserCondition)

            val option = if (remainingPages[0] == "!MinecraftItem") {
                BookSerializable.getObjectAndRemainingPages(remainingPages.drop(1), MinecraftItem).first
            } else if (remainingPages[0] == "!HealthHungerOption") {
                BookSerializable.getObjectAndRemainingPages(remainingPages.drop(1), HealthHungerOption).first
            } else if (remainingPages[0] == "!Int") {
                remainingPages[1].toInt()
            } else if (remainingPages[0] == "!MinecraftItemList") {
                BookSerializable.getListAndRemainingPages(remainingPages.drop(1), MinecraftItem).first
            } else {
                throw IllegalArgumentException("Expected !MinecraftItem or !MinecraftItemList whilst deserializing WeightedOption but found ${remainingPages[0]}")
            }

            return WeightedOption(option, weight, label, conditions)
        }
    }
}