package me.repeater64.advancedmpkeditor.backend.data_object.random_slot

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import me.repeater64.advancedmpkeditor.backend.data_object.ContentHashable
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.item.DontReplaceMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.util.hash

@Stable
class RandomSlotOptionsSet(_setName: String, _options: WeightedOptionList<SnapshotStateList<MinecraftItem>>) : ContentHashable {
    var setName by mutableStateOf(_setName)
    val options by mutableStateOf(_options)

    override fun contentHash() = hash(setName, options.contentHash())

    val numStacksProperty: (List<MinecraftItem>) -> Int = { it.fold(0) { acc, item -> acc + item.numStacks } }
    val maxNumStacks get() = options.getMaximumPossibleProperty(numStacksProperty)
    fun canAlwaysFitInOneSlot(): Boolean = !options.options.any { itemList -> itemList.option.sumOf { it.numStacks } > 1 }

    fun nameSelf() {
        val itemNames = mutableSetOf<String>()
        for (option in options.options) {
            if (option.option.isEmpty()) continue
            val firstItem = option.option.first()
            if (firstItem is DontReplaceMinecraftItem) {
                itemNames.add(DontReplaceMinecraftItem(true).displayName)
            } else {
                val name = firstItem.displayName
                if (firstItem.amount > 1) {
                    // Make plural
                    if (name.endsWith("s") || name.endsWith("wool") || name.endsWith("powder") || name.endsWith("string") || name.endsWith("dust")) {
                        itemNames.add(name)
                    } else {
                        itemNames.add(name + "s")
                    }
                } else {
                    itemNames.add(name)
                }
            }
        }

        val sb = StringBuilder()
        for ((index, itemName) in itemNames.withIndex()) {
            sb.append(itemName)
            if (index == 5) {
                sb.append(" / ...")
                break
            }
            if (index != itemNames.size-1) {
                sb.append(" / ")
            }
        }

        setName = sb.toString()
    }

    companion object : BookSerializable<RandomSlotOptionsSet> {
        override val className = "RandomSlotOptionsSet"

        override fun serializeToPages(it: RandomSlotOptionsSet): List<String> {
            return listOf(it.setName) + WeightedOptionList.serialize(it.options as WeightedOptionList<Any>)
        }

        override fun deserializeFromPages(pages: List<String>): RandomSlotOptionsSet {
            return RandomSlotOptionsSet(pages[0], BookSerializable.getObjectAndRemainingPages(pages.drop(1), WeightedOptionList).first as WeightedOptionList<SnapshotStateList<MinecraftItem>>)
        }
    }
}