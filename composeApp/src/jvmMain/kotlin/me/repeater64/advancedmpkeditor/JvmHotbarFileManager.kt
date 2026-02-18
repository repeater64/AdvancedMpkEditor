package me.repeater64.advancedmpkeditor

import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbars
import me.repeater64.advancedmpkeditor.gui.platform.HotbarNbtFileManager
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer

class JvmHotbarFileManager : HotbarNbtFileManager {
    private val workingDir = "".toPath()

    override fun saveHotbars(hotbars: SavedHotbars, fileName: String) {
        val path = workingDir.resolve(fileName)
        FileSystem.SYSTEM.sink(path).buffer().use { sink ->
            hotbars.encodeToNbtSink(sink)
        }
    }

    override suspend fun loadHotbars(): SavedHotbars? {
        val path = workingDir.resolve("hotbar.nbt")

        if (!FileSystem.SYSTEM.exists(path)) return null

        return FileSystem.SYSTEM.source(path).buffer().use { source ->
            try {
                SavedHotbars.decodeFromNbtSource(source)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }
    }
}