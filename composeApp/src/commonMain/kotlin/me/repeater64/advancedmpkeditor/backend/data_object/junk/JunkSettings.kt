package me.repeater64.advancedmpkeditor.backend.data_object.junk

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import me.repeater64.advancedmpkeditor.backend.data_object.ContentHashable
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionNoLinks
import me.repeater64.advancedmpkeditor.util.hash

@Stable
class JunkSettings(
    _enableJunk: Boolean,
    _makeJunkNonStackable: Boolean,
    _junkList: List<WeightedOptionNoLinks<MinecraftItem>>
) : ContentHashable {
    var enableJunk by mutableStateOf(_enableJunk)
    var makeJunkNonStackable by mutableStateOf(_makeJunkNonStackable)
    val junkList = _junkList.toMutableStateList()

    override fun contentHash() = hash(enableJunk, makeJunkNonStackable, junkList.map { it.contentHash() })

    fun getActualJunkList(): List<MinecraftItem> {
        val list = mutableListOf<MinecraftItem>()
        for (option in junkList) {
            repeat(option.weight) { list.add(option.option) }
        }
        return list.shuffled()
    }

    companion object : BookSerializable<JunkSettings> {
        override val className = "JunkSettings"

        override fun serializeToPages(it: JunkSettings): List<String> {
            return listOf(it.enableJunk.toString(), it.makeJunkNonStackable.toString()) + BookSerializable.serializeList(it.junkList, WeightedOptionNoLinks as BookSerializable<WeightedOptionNoLinks<MinecraftItem>>)
        }

        override fun deserializeFromPages(pages: List<String>): JunkSettings {
            return JunkSettings(pages[0].toBoolean(), pages[1].toBoolean(), BookSerializable.getListAndRemainingPages(pages.drop(2), WeightedOptionNoLinks).first as List<WeightedOptionNoLinks<MinecraftItem>>)
        }
    }
}