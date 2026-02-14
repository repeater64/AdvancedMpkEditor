package me.repeater64.advancedmpkeditor.gui.platform

import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.awtTransferable
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbars
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import okio.buffer
import java.awt.datatransfer.DataFlavor
import java.io.File

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun FileDropZone(
    modifier: Modifier,
    onFileDropped: (SavedHotbars?) -> Unit,
    content: @Composable () -> Unit
) {
    val dropTarget = remember {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                val transferable = event.awtTransferable

                if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    try {
                        @Suppress("UNCHECKED_CAST")
                        val fileList = transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>

                        val firstFile = fileList.firstOrNull()
                        if (firstFile != null) {
                            val path = firstFile.toOkioPath()
                            FileSystem.SYSTEM.source(path).buffer().use { source ->
                                val hotbars = SavedHotbars.decodeFromNbtSource(source)
                                onFileDropped(hotbars)
                            }
                            return true
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        return false
                    }
                }
                return false
            }
        }
    }

    Box(
        modifier = modifier.dragAndDropTarget(
            shouldStartDragAndDrop = { event ->
                event.awtTransferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)
            },
            target = dropTarget
        ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}