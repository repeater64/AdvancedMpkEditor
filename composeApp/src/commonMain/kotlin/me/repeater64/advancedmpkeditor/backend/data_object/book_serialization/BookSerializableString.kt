package me.repeater64.advancedmpkeditor.backend.data_object.book_serialization

class BookSerializableString(val string: String) {
    companion object : BookSerializable<BookSerializableString> {
        override val className = "BookSerializableString"

        override fun serializeToPages(it: BookSerializableString): List<String> {
            return listOf(it.string)
        }

        override fun deserializeFromPages(pages: List<String>): BookSerializableString {
            return BookSerializableString(pages[0])
        }

    }
}