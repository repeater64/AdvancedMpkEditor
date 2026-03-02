package me.repeater64.advancedmpkeditor.backend.commands

import androidx.compose.runtime.toMutableStateList
import me.repeater64.advancedmpkeditor.backend.data_object.ContentHashable
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializableString
import me.repeater64.advancedmpkeditor.util.hash

class CustomCommandSettings(
    _preItemsCommands: List<String>,
    _postItemsCommands: List<String>,
    _postTeleportCommands: List<String>
) : ContentHashable {
    val preItemsCommands = _preItemsCommands.toMutableStateList()
    val postItemsCommands = _postItemsCommands.toMutableStateList()
    val postTeleportCommands = _postTeleportCommands.toMutableStateList()

    override fun contentHash(): Int {
        return hash(preItemsCommands.toList(), postItemsCommands.toList(), postTeleportCommands.toList())
    }

    companion object : BookSerializable<CustomCommandSettings> {
        override val className = "CustomCommandSettings"

        override fun serializeToPages(it: CustomCommandSettings): List<String> {
            return BookSerializable.serializeList(it.preItemsCommands.map { BookSerializableString(it) }, BookSerializableString) +
                    BookSerializable.serializeList(it.postItemsCommands.map { BookSerializableString(it) }, BookSerializableString) +
                    BookSerializable.serializeList(it.postTeleportCommands.map { BookSerializableString(it) }, BookSerializableString)
        }

        override fun deserializeFromPages(pages: List<String>): CustomCommandSettings {
            val (preItemsCommands, pages1) = BookSerializable.getListAndRemainingPages(pages, BookSerializableString)
            val (postItemsCommands, pages2) = BookSerializable.getListAndRemainingPages(pages1, BookSerializableString)
            val postTeleportCommands = BookSerializable.getListAndRemainingPages(pages2, BookSerializableString).first
            return CustomCommandSettings(preItemsCommands.map { it.string }, postItemsCommands.map { it.string }, postTeleportCommands.map { it.string })
        }
    }
}