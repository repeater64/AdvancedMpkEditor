package me.repeater64.advancedmpkeditor.gui.platform

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.browser.window
import org.w3c.dom.DragEvent
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbars
import okio.Buffer
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.w3c.dom.events.Event
import org.w3c.files.File
import org.w3c.files.FileReader
import org.w3c.files.get
import kotlin.coroutines.resume

@Composable
actual fun FileDropZone(
    modifier: Modifier,
    onFileDropped: (SavedHotbars?) -> Unit,
    content: @Composable (() -> Unit)
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        content()
    }

    DisposableEffect(Unit) {
        val scope = MainScope()

        val onDragEnter = { e: DragEvent ->
            e.preventDefault()
        }

        val onDragOver = { e: DragEvent ->
            e.preventDefault()
            e.dataTransfer?.dropEffect = "copy"
        }

        val onDrop = { e: DragEvent ->
            e.preventDefault()

            val files = e.dataTransfer?.files
            if (files != null && files.length > 0) {
                val file = files[0]!!
                scope.launch {
                    val result = readFileFromDrop(file)
                    onFileDropped(result)
                }
            }
        }

        window.addEventListener("dragenter", onDragEnter as (Event) -> Unit)
        window.addEventListener("dragover", onDragOver as (Event) -> Unit)
        window.addEventListener("drop", onDrop as (Event) -> Unit)

        // Cleanup
        onDispose {
            window.removeEventListener("dragenter", onDragEnter)
            window.removeEventListener("dragover", onDragOver)
            window.removeEventListener("drop", onDrop)
        }
    }
}

suspend fun readFileFromDrop(file: File): SavedHotbars? = suspendCancellableCoroutine { cont ->
    val reader = FileReader()
    reader.onload = { loadEvent ->
        val arrayBuffer = loadEvent.target.asDynamic().result as ArrayBuffer
        val uint8Array = Uint8Array(arrayBuffer)
        val byteArray = ByteArray(uint8Array.length)
        for (i in 0 until uint8Array.length) {
            byteArray[i] = uint8Array[i]
        }
        val source = Buffer().write(byteArray)
        try {
            cont.resume(SavedHotbars.decodeFromNbtSource(source))
        } catch (e: Exception) {
            cont.resume(null)
        }
    }
    reader.readAsArrayBuffer(file)
}