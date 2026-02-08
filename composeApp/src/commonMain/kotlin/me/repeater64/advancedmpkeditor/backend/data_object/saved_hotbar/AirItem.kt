package me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar

import net.benwoodworth.knbt.NbtCompound
import net.benwoodworth.knbt.buildNbtCompound
import net.benwoodworth.knbt.put

class AirItem : SavedHotbarItem() {
    override fun getTag(): NbtCompound {
        return buildNbtCompound {
            put("id", "minecraft:air")
            put("Count", 1.toByte())
//            putNbtCompound("tag") {
//                put("Charged", 0.toByte())
//            }
        }
    }
}