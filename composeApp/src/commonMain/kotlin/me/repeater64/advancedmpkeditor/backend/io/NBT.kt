package me.repeater64.advancedmpkeditor.backend.io

import net.benwoodworth.knbt.Nbt
import net.benwoodworth.knbt.NbtByte
import net.benwoodworth.knbt.NbtCompound
import net.benwoodworth.knbt.NbtCompression
import net.benwoodworth.knbt.NbtDouble
import net.benwoodworth.knbt.NbtInt
import net.benwoodworth.knbt.NbtShort
import net.benwoodworth.knbt.NbtVariant

object NBT {
    val nbt = Nbt {
        variant = NbtVariant.Java
        compression = NbtCompression.None
    }

    fun getNumber(tag: NbtCompound, key: String, default: Int): Int {
        if (tag.containsKey(key)) {
            return when (val countTag = tag[key]) {
                is NbtByte -> countTag.value.toInt()
                is NbtDouble -> countTag.value.toInt()
                is NbtShort -> countTag.value.toInt()
                is NbtInt -> countTag.value
                else -> default
            }
        }
        return default
    }

    fun getCount(tag: NbtCompound): Int {
        return getNumber(tag, "Count", 1) // Needed because it seems Minecraft will accept Count tag in any number format, so some external tools that generate hotbar.nbt files may take advantage of this by saving in the wrong format
    }
}