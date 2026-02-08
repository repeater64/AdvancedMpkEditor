package me.repeater64.advancedmpkeditor.backend.data_object.trigger_items

import net.benwoodworth.knbt.NbtCompound
import net.benwoodworth.knbt.add
import net.benwoodworth.knbt.buildNbtCompound
import net.benwoodworth.knbt.put
import net.benwoodworth.knbt.putNbtCompound
import net.benwoodworth.knbt.putNbtList

data class WritableAutoBookItem(override val pages: List<String>) : BookItem(pages) {
    override fun getNbt(slot: Int): NbtCompound {
        return buildNbtCompound {
            put("Count", 1.toByte())
            put("Slot", slot.toByte())
            put("id", "minecraft:writable_book")
            putNbtCompound("tag") {
                putNbtList("pages") {
                    pages.forEach { add(it) }
                }
                putNbtCompound("display") {
                    put("Name", "{\"text\":\"AUTO\"}")
                    putNbtList("Lore") {
                        add("{\"text\":\"repeater64's Advanced MPK Editor - Generated Commands - Don't Edit\"}") // TODO put github URL in another lore line
                    }
                }
            }
        }
    }

}