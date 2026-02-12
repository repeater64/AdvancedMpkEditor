package me.repeater64.advancedmpkeditor

import kotlinx.browser.document
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbars
import me.repeater64.advancedmpkeditor.gui.platform.HotbarNbtFileManager
import okio.Buffer
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.khronos.webgl.set
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import org.w3c.files.FileReader
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class JsHotbarFileManager : HotbarNbtFileManager {

    override fun saveHotbars(hotbars: SavedHotbars, fileName: String) {
        // 1. Write Hotbars to an Okio Buffer
        val buffer = Buffer()
        hotbars.encodeToNbtSink(buffer)

        // 2. Convert Buffer to JS Blob
        val byteArray = buffer.readByteArray()
        // Convert Kotlin ByteArray to JS Int8Array
        val int8Array = Int8Array(byteArray.size)
        for (i in byteArray.indices) {
            int8Array[i] = byteArray[i]
        }

        val blob = Blob(arrayOf(int8Array), BlobPropertyBag(type = "application/octet-stream"))

        // 3. Trigger Download via Anchor tag
        val url = URL.createObjectURL(blob)
        val link = document.createElement("a") as HTMLAnchorElement
        link.href = url
        link.download = fileName
        document.body?.appendChild(link)
        link.click()
        document.body?.removeChild(link)
        URL.revokeObjectURL(url)
    }

    override suspend fun loadHotbars(): SavedHotbars? = suspendCoroutine { continuation ->
        // 1. Create invisible file input
        val input = document.createElement("input") as HTMLInputElement
        input.type = "file"
        input.style.display = "none"

        input.onchange = { event ->
            val file = input.files?.item(0)
            if (file != null) {
                val reader = FileReader()
                reader.onload = { loadEvent ->
                    // 2. Read ArrayBuffer -> ByteArray -> Okio Buffer
                    val arrayBuffer = loadEvent.target.asDynamic().result as ArrayBuffer
                    val uint8Array = Uint8Array(arrayBuffer)
                    val byteArray = ByteArray(uint8Array.length)
                    for (i in 0 until uint8Array.length) {
                        byteArray[i] = uint8Array[i]
                    }

                    val source = Buffer().write(byteArray)

                    // 3. Decode
                    try {
                        val result = SavedHotbars.decodeFromNbtSource(source)
                        continuation.resume(result)
                    } catch (e: Exception) {
                        console.error("Failed to decode NBT", e)
                        continuation.resume(null)
                    }
                }
                reader.readAsArrayBuffer(file)
            } else {
                continuation.resume(null)
            }
        }

        // 4. Trigger the dialog
        document.body?.appendChild(input)
        input.click()
        document.body?.removeChild(input)
    }
}