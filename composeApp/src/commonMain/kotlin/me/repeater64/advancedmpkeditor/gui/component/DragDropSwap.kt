package me.repeater64.advancedmpkeditor.gui.component

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.round
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

class DragDropState(val onSwap: (srcKey: Int, destKey: Int) -> Unit, val emptyChecker: (key: Int) -> Boolean) {
    var draggingFromKey by mutableStateOf<Int?>(null)
    var dragGhostContent by mutableStateOf<(@Composable () -> Unit)?>(null)
    var dragPosition by mutableStateOf(Offset.Zero)
    var dragOffset by mutableStateOf(Offset.Zero) // Offset within the item being dragged

    private val targets = mutableMapOf<Int, Rect>()

    fun registerTarget(key: Int, coordinates: LayoutCoordinates) {
        if (coordinates.isAttached) {
            targets[key] = coordinates.boundsInWindow()
        }
    }

    fun unregisterTarget(key: Int) {
        targets.remove(key)
    }

    fun canStartDrag(fromKey: Int) : Boolean = !emptyChecker(fromKey)

    fun onDragEnd() {
        val fromKey = draggingFromKey ?: return
        val dropPosition = dragPosition

        val hitTargetKey = targets.entries.firstOrNull { (_, bounds) ->
            bounds.contains(dropPosition)
        }?.key

        if (hitTargetKey != null) {
            onSwap(fromKey, hitTargetKey)
        }
        cleanup()
    }

    fun cleanup() {
        draggingFromKey = null
        dragGhostContent = null
    }
}

val LocalDragDropState = compositionLocalOf<DragDropState> { error("No DragDropContainer found") }

@Composable
fun DragDropContainer(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    onSwap: (srcKey: Int, destKey: Int) -> Unit,
    emptyChecker: (key: Int) -> Boolean,
    content: @Composable BoxScope.() -> Unit
) {
    val state = remember { DragDropState(onSwap, emptyChecker) }

    var currentWindowPosition by remember { mutableStateOf(Offset.Zero) }

    CompositionLocalProvider(LocalDragDropState provides state) {
        Box(modifier = modifier
                        .onGloballyPositioned { coordinates -> currentWindowPosition = coordinates.boundsInWindow().topLeft},
            contentAlignment = contentAlignment) {
            content()

            // The ghost
            if (state.draggingFromKey != null && state.dragGhostContent != null) {
                val offset = (state.dragPosition + state.dragOffset - currentWindowPosition).round()

                Popup(
                    offset = IntOffset(offset.x, offset.y),
                    properties = PopupProperties(focusable = false, clippingEnabled = false)
                ) {
                    state.dragGhostContent!!()
                }
            }
        }
    }
}
@Composable
fun DragSwappable(
    key: Int,
    modifier: Modifier = Modifier,
    ghostContent: @Composable () -> Unit,
    content: @Composable (isDragging: Boolean) -> Unit
) {
    val state = LocalDragDropState.current

    var currentWindowPosition by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                currentWindowPosition = coordinates.boundsInWindow().topLeft
                state.registerTarget(key, coordinates)
            }
            .pointerInput(key) {
                detectDragGestures(
                    onDragStart = { offset ->
                        if (!state.canStartDrag(key)) return@detectDragGestures
                        state.draggingFromKey = key
                        state.dragGhostContent = ghostContent
                        state.dragPosition = currentWindowPosition + offset
                        state.dragOffset = -offset
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        state.dragPosition += dragAmount
                    },
                    onDragEnd = { state.onDragEnd() },
                    onDragCancel = { state.cleanup() }
                )
            }
    ) {
        val isDragging = state.draggingFromKey == key
        Box {
            content(isDragging)
        }
    }

    DisposableEffect(key) {
        onDispose { state.unregisterTarget(key) }
    }
}