package me.repeater64.advancedmpkeditor.backend.data_object.trigger_items

import net.benwoodworth.knbt.NbtCompound
import net.benwoodworth.knbt.add
import net.benwoodworth.knbt.buildNbtCompound
import net.benwoodworth.knbt.put
import net.benwoodworth.knbt.putNbtCompound
import net.benwoodworth.knbt.putNbtList

data class WrittenBookItem(override val pages: List<String>) : BookItem(pages) {
    override fun getNbt(slot: Int): NbtCompound {
        return buildNbtCompound {
            put("Count", 1.toByte())
            put("Slot", slot.toByte())
            put("id", "minecraft:written_book")
            putNbtCompound("tag") {
                putNbtList("pages") {
                    pages.forEach { add(it) }
                }
                put("author", "repeater64")
                put("title", "Important Data used for Advanced MPK Editor - Don't Edit")
            }
        }
    }

}