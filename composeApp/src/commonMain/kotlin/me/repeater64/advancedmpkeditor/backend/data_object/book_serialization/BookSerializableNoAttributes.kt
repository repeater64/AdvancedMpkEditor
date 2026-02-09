package me.repeater64.advancedmpkeditor.backend.data_object.book_serialization

interface BookSerializableNoAttributes<T> : BookSerializable<T> {
    override fun serializeToPages(it: T): List<String> {
        return listOf(className)
    }

    override fun deserializeFromPages(pages: List<String>): T {
        return createObject()
    }

    fun createObject(): T
}