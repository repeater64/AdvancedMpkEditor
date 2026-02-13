package me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar

import androidx.compose.runtime.Stable
import androidx.compose.runtime.toMutableStateList
import me.repeater64.advancedmpkeditor.backend.data_object.ContentHashable
import me.repeater64.advancedmpkeditor.backend.io.NBT.nbt
import me.repeater64.advancedmpkeditor.util.hash
import net.benwoodworth.knbt.NbtCompound
import net.benwoodworth.knbt.NbtList
import net.benwoodworth.knbt.OkioApi
import net.benwoodworth.knbt.buildNbtCompound
import net.benwoodworth.knbt.put
import net.benwoodworth.knbt.putNbtCompound
import okio.Sink
import okio.Source
import kotlin.collections.forEach

@Stable
class SavedHotbars(_hotbars: List<SavedHotbar> = emptyList()) : ContentHashable {
    val hotbars = _hotbars.toMutableStateList()

    override fun contentHash(): Int {
        return hash(hotbars.map { it.contentHash() })
    }

    @OptIn(OkioApi::class)
    fun encodeToNbtSink(sink: Sink) {
        nbt.encodeToSink(NbtCompound.serializer(), getTag(), sink)
    }

    private fun getTag(): NbtCompound {
        return buildNbtCompound {
            putNbtCompound("") {
                for ((index, hotbar) in hotbars.withIndex()) {
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
            val list = MutableList(9) {SavedHotbar(emptyList())}
            actualTag.forEach { (key, value) -> if (key != "DataVersion") list[key.toInt()] = SavedHotbar.fromTag(value as NbtList<NbtCompound>) }
            return SavedHotbars(list)
        }
    }
}