package me.repeater64.advancedmpkeditor.backend.data_object.misc_options

import me.repeater64.advancedmpkeditor.backend.io.NBT
import net.benwoodworth.knbt.NbtByte
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
        fun <T : Enum<T>> tryGetFromNbt(tag: NbtCompound, enumEntries: EnumEntries<T>) : ItemBasedOptionEnum? {
            val id = (tag["id"] as NbtString).value

            val amountMustMatch = if (id == "minecraft:obsidian" || id == "minecraft:end_portal_frame") {
                // Need to check count too
                NBT.getCount(tag)
            } else null

            for (enum in enumEntries) {
                if ((enum as ItemBasedOptionEnum).minecraftItemId == id) {
                    amountMustMatch?.let { if (enum.amount != it) continue }
                    return enum
                }
            }
            return null
        }
        fun <T : Enum<T>> getFromNbt(tag: NbtCompound, enumEntries: EnumEntries<T>) : ItemBasedOptionEnum {
            return tryGetFromNbt(tag, enumEntries) ?: throw IllegalArgumentException("Invalid item ${(tag["id"] as NbtString).value} for an ItemBasedOptionEnum")
        }
    }
}