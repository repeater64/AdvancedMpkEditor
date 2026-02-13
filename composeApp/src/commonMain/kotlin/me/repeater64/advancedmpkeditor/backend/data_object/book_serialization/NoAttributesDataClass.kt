package me.repeater64.advancedmpkeditor.backend.data_object.book_serialization

import me.repeater64.advancedmpkeditor.backend.data_object.ContentHashable

abstract class NoAttributesDataClass : ContentHashable {
    override fun equals(other: Any?): Boolean {
        // Check if the other object is exactly the same class as this instance
        return if (other == null) false else this::class == other::class
    }

    override fun hashCode(): Int {
        return this::class.hashCode()
    }

    override fun toString(): String {
        return this::class.simpleName ?: "NoAttributesDataClass"
    }

    override fun contentHash(): Int {
        return hashCode()
    }
}