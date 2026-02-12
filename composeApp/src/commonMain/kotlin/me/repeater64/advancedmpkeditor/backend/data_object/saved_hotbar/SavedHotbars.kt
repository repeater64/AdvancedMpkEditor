package me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar

import me.repeater64.advancedmpkeditor.backend.io.NBT.nbt
import net.benwoodworth.knbt.NbtCompound
import net.benwoodworth.knbt.NbtList
import net.benwoodworth.knbt.OkioApi
import net.benwoodworth.knbt.buildNbtCompound
import net.benwoodworth.knbt.put
import net.benwoodworth.knbt.putNbtCompound
import okio.Sink
import okio.Source
import kotlin.collections.iterator

data class SavedHotbars(val hotbars: MutableMap<Int, SavedHotbar> = hashMapOf()) {
    @OptIn(OkioApi::class)
    fun encodeToNbtSink(sink: Sink) {
        nbt.encodeToSink(NbtCompound.serializer(), getTag(), sink)
    }

    private fun getTag(): NbtCompound {
        return buildNbtCompound {
            putNbtCompound("") {
                for ((index, hotbar) in hotbars) {
                     put(index.toString(), hotbar.getTag())
                }
                put("DataVersion", 2567)
            }
        }
    }

    companion object {
        @OptIn(OkioApi::class)
        fun decodeFromNbtSource(source: Source) : SavedHotbars {
            return fromTag(nbt.decodeFromSource(NbtCompound.serializer(), source))
        }

        private fun fromTag(tag: NbtCompound) : SavedHotbars {
            val actualTag = tag[""] as NbtCompound
            val map = mutableMapOf<Int, SavedHotbar>()
            actualTag.forEach { (key, value) -> if (key != "DataVersion") map[key.toInt()] = SavedHotbar.fromTag(value as NbtList<NbtCompound>) }
            return SavedHotbars(map)
        }
    }
}