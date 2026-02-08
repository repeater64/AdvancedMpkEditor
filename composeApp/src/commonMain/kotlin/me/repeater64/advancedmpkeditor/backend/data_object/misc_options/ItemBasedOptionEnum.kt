package me.repeater64.advancedmpkeditor.backend.data_object.misc_options

import net.benwoodworth.knbt.NbtCompound
import net.benwoodworth.knbt.NbtString
import net.benwoodworth.knbt.buildNbtCompound
import net.benwoodworth.knbt.put
import kotlin.enums.EnumEntries


interface ItemBasedOptionEnum {
    val minecraftItemId: String
    val amount: Int
    val displayName: String

    fun getItemNBT(slot: Int): NbtCompound {
        return buildNbtCompound {
            put("id", minecraftItemId)
            put("Count", amount.toByte())
            put("Slot", slot.toByte())
        }
    }

    companion object {
        fun <T : Enum<T>> getFromNbt(tag: NbtCompound, enumEntries: EnumEntries<T>) : ItemBasedOptionEnum {
            val id = (tag["id"] as NbtString).value

            for (enum in enumEntries) {
                if ((enum as ItemBasedOptionEnum).minecraftItemId == id) {
                    return enum
                }
            }
            throw IllegalArgumentException("Invalid item $id for an ItemBasedOptionEnum")
        }
    }
}