package me.repeater64.advancedmpkeditor.backend

import net.benwoodworth.knbt.Nbt
import net.benwoodworth.knbt.NbtCompression
import net.benwoodworth.knbt.NbtVariant

object NBT {
    val nbt = Nbt {
        variant = NbtVariant.Java
        compression = NbtCompression.None
    }
}