package me.repeater64.advancedmpkeditor.backend.data_object.book_serialization

abstract class NoAttributesDataClass {
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
}