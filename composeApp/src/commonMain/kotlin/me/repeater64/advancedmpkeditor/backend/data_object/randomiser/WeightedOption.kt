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
) : Comparable<WeightedOption<T>>, ContentHashable, WeightedOptionEitherType<T> {
    override var option by mutableStateOf(_option)
    override var weight by mutableStateOf(_weight)
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

    override fun copyWithNewWeight(newWeight: Int): WeightedOptionEitherType<T> {
        return WeightedOption(this.option, newWeight, this.label, this.conditions)
    }

    fun deepCopy(): WeightedOption<T> {
        // Rather than implementing a proper deep copy which would require changes to many classes, just serialize + deserialize. This isn't efficient but this doesn't particularly need to be, it's ok if we freeze the GUI for a moment when someone duplicates a row
        return deserializeFromPages(serializeToPages(this as WeightedOption<Any>)) as WeightedOption<T>
    }

    companion object : BookSerializable<WeightedOption<Any>> {
        override val className: String = "WeightedOption"

        override fun serializeToPages(it: WeightedOption<Any>): List<String> {
            val pages = mutableListOf<String>()
            pages.add(it.label.toString())
            pages.addAll(BookSerializable.serializeList(it.conditions, RandomiserCondition))
            pages.addAll(WeightedOptionNoLinks.serialize(WeightedOptionNoLinks(it.option, it.weight)))
            return pages
        }

        override fun deserializeFromPages(pages: List<String>): WeightedOption<Any> {
            val label = if (pages[0] == "null") null else pages[0]
            val (conditions, remainingPages) = BookSerializable.getListAndRemainingPages(pages.drop(1), RandomiserCondition)
            val weightedOptionNoLinks = BookSerializable.getObjectAndRemainingPages(remainingPages, WeightedOptionNoLinks).first

            return WeightedOption(weightedOptionNoLinks.option, weightedOptionNoLinks.weight, label, conditions)
        }
    }
}