package me.repeater64.advancedmpkeditor.gui.component

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.ui.composed
import kotlinx.coroutines.delay

class DragDropState(val onSwap: (srcKey: Int, destKey: Int) -> Unit, val emptyChecker: (key: Int) -> Boolean) {
    var draggingFromKey by mutableStateOf<Int?>(null)
    var dragGhostContent by mutableStateOf<(@Composable () -> Unit)?>(null)
    var dragPosition by mutableStateOf(Offset.Zero)
    var dragOffset by mutableStateOf(Offset.Zero) // Offset within the item being dragged

    private val targets = mutableStateMapOf<Int, Rect>()

    val currentHoverTarget: Int? by derivedStateOf {
        if (draggingFromKey == null) null
        else targets.entries.firstOrNull { (_, bounds) ->
            bounds.contains(dragPosition)
        }?.key
    }

    fun registerTarget(key: Int, coordinates: LayoutCoordinates) {
        if (coordinates.isAttached) {
            targets[key] = coordinates.boundsInRoot()
        }
    }

    fun unregisterTarget(key: Int) {
        targets.remove(key)
    }

    fun canStartDrag(fromKey: Int) : Boolean = !emptyChecker(fromKey)

    fun onDragEnd() {
        val fromKey = draggingFromKey ?: return

        val hitTargetKey = currentHoverTarget

        if (hitTargetKey != null && hitTargetKey != fromKey) {
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
        Box(
            modifier = modifier.fillMaxSize().onGloballyPositioned {
                currentWindowPosition = it.boundsInWindow().topLeft
            },
            contentAlignment = contentAlignment
        ) {
            content()

            // The ghost
            if (state.draggingFromKey != null && state.dragGhostContent != null) {
                Box(
                    modifier = Modifier.align(Alignment.TopStart).offset {
                        IntOffset(
                            x = (state.dragPosition.x + state.dragOffset.x - currentWindowPosition.x).roundToInt(),
                            y = (state.dragPosition.y + state.dragOffset.y - currentWindowPosition.y).roundToInt()
                        )
                    }
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
    accountForOffset: Boolean = true,
    ghostContent: @Composable () -> Unit,
    content: @Composable (isDragging: Boolean, isHovered: Boolean) -> Unit
) {
    val state = LocalDragDropState.current
    var rootPosition by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                rootPosition = coordinates.boundsInWindow().topLeft
                state.registerTarget(key, coordinates)
            }
            .pointerInput(key) {
                detectDragGestures(
                    onDragStart = { offset ->
                        if (!state.canStartDrag(key)) return@detectDragGestures
                        state.draggingFromKey = key
                        state.dragGhostContent = ghostContent
                        state.dragPosition = rootPosition + offset
                        state.dragOffset = if (accountForOffset) -offset else Offset.Zero
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
        val isHovered = state.currentHoverTarget == key && !isDragging
        content(isDragging, isHovered)
    }

    DisposableEffect(key) {
        onDispose { state.unregisterTarget(key) }
    }
}

fun Modifier.dragAutoScroll(
    scrollState: ScrollableState,
    threshold: Float = 100f,
    maxSpeed: Float = 20f
): Modifier = composed {
    val dragDropState = LocalDragDropState.current
    var containerBounds by remember { mutableStateOf<Rect?>(null) }

    LaunchedEffect(dragDropState.draggingFromKey) {
        // Only start the loop if an item is actively being dragged
        if (dragDropState.draggingFromKey != null) {
            while (true) {
                val bounds = containerBounds
                if (bounds != null) {
                    val dragY = dragDropState.dragPosition.y
                    var scrollAmount = 0f

                    // Top trigger zone
                    if (dragY < bounds.top + threshold) {
                        val intensity = 1f - ((dragY - bounds.top) / threshold).coerceIn(0f, 1f)
                        scrollAmount = -maxSpeed * intensity
                    }
                    // Bottom trigger zone
                    else if (dragY > bounds.bottom - threshold) {
                        val intensity = 1f - ((bounds.bottom - dragY) / threshold).coerceIn(0f, 1f)
                        scrollAmount = maxSpeed * intensity
                    }

                    if (scrollAmount != 0f) {
                        scrollState.scrollBy(scrollAmount)
                    }
                }
                // Wait a bit for the next frame
                delay(16)
            }
        }
    }

    this.onGloballyPositioned { coordinates ->
        containerBounds = coordinates.boundsInRoot()
    }
}