package me.repeater64.advancedmpkeditor.backend.data_object.book_serialization

interface BookSerializable<T> {
    val className: String
    fun serializeToPages(it: T): List<String>
    fun deserializeFromPages(pages: List<String>): T

    fun serialize(it: T): List<String> {
        return listOf("!BEGIN $className") + serializeToPages(it) + listOf("!END $className")
    }

    companion object {
        fun <Companion : BookSerializable<A>, A> getObjectAndRemainingPages(pages: List<String>, companionObject: Companion): Pair<A, List<String>> {
            val className = companionObject.className
            if (pages.size < 3) {
                throw IllegalArgumentException("getObjectAndRemainingPages() called with not enough pages (attempting to get a $className object from pages: $pages)")
            }
            if (pages[0] != "!BEGIN $className") {
                throw IllegalArgumentException("getObjectAndRemainingPages() called but the object found isn't correct (attempting to get a $className object but page 1 is: ${pages[0]})")
            }
            val relevantPages = mutableListOf<String>()
            var foundEnd = false
            for (page in pages.drop(1)) {
                if (page == "!END $className") {
                    foundEnd = true
                    break
                }
                relevantPages.add(page)
            }

            if (!foundEnd) {
                throw IllegalArgumentException("getObjectAndRemainingPages() called but no object end found (attempting to get a $className object from pages: $pages)")
            }

            val obj = companionObject.deserializeFromPages(relevantPages)
            val remainingPages = pages.drop(relevantPages.size + 2)
            return Pair(obj, remainingPages)
        }

        fun <Companion : BookSerializable<A>, A> serializeList(list: Iterable<A>, companionObject: Companion) : List<String> {
            return listOf("!BEGINLIST") + list.flatMap { companionObject.serialize(it) } + listOf("!ENDLIST")
        }

        fun <Companion : BookSerializable<A>, A> getListAndRemainingPages(pages: List<String>, companionObject: Companion): Pair<List<A>, List<String>> {
            if (pages.size < 2) {
                throw IllegalArgumentException("getListAndRemainingPages() called with not enough pages (pages: $pages)")
            }
            if (pages[0] != "!BEGINLIST") {
                throw IllegalArgumentException("getListAndRemainingPages() called but this isn't a list (first page: ${pages[0]})")
            }
            var remainingPages = pages.drop(1)
            var list = mutableListOf<A>()
            while (true) {
                if (remainingPages[0] == "!ENDLIST") {
                    remainingPages = remainingPages.drop(1)
                    break
                }

                val pair = getObjectAndRemainingPages(remainingPages, companionObject)
                list.add(pair.first)
                remainingPages = pair.second

                if (remainingPages.isEmpty()) {
                    throw IllegalArgumentException("getListAndRemainingPages() called but no ENDLIST tag was found (pages: $pages)")
                }
            }
            return Pair(list, remainingPages)
        }
    }
}