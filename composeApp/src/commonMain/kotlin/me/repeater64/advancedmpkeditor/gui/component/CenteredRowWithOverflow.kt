package me.repeater64.advancedmpkeditor.gui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max

@Composable
fun CenteredRowWithOverflow(
    modifier: Modifier = Modifier,
    spacing: Dp = 8.dp,
    mainContent: @Composable () -> Unit,
    trailingContent: @Composable () -> Unit
) {
    Layout(
        content = {
            // 1. Wrap content in Boxes so they are always treated as single units
            Box { mainContent() }
            Box { trailingContent() }
        },
        modifier = modifier
    ) { measurables, constraints ->

        // 2. Measure the wrappers
        // We use minWidth=0 to ensure we don't force them to expand if not needed
        val looseConstraints = constraints.copy(minWidth = 0)

        val mainPlaceable = measurables[0].measure(looseConstraints)
        val trailingPlaceable = measurables[1].measure(looseConstraints)

        val spacingPx = spacing.roundToPx()
        val containerWidth = constraints.maxWidth

        // 3. Calculate Geometry
        val mainWidth = mainPlaceable.width
        val trailingWidth = trailingPlaceable.width
        val totalWidthIfNeeded = mainWidth + spacingPx + trailingWidth

        // Ideally, Main is at center: (Container - Main) / 2
        val mainXCentered = (containerWidth - mainWidth) / 2

        // Trailing is to the right of that
        val trailingX = mainXCentered + mainWidth + spacingPx

        // Check if the trailing item fits within the bounds
        val fitsOnOneLine = (trailingX + trailingWidth) <= containerWidth

        if (fitsOnOneLine) {
            // --- Single Line Layout ---
            val height = max(mainPlaceable.height, trailingPlaceable.height)
            layout(containerWidth, height) {
                // Center Vertically
                val mainY = (height - mainPlaceable.height) / 2
                val trailingY = (height - trailingPlaceable.height) / 2

                mainPlaceable.placeRelative(mainXCentered, mainY)
                trailingPlaceable.placeRelative(trailingX, trailingY)
            }
        } else {
            // --- Stacked Layout ---
            val height = mainPlaceable.height + spacingPx + trailingPlaceable.height
            layout(containerWidth, height) {
                // Main: Centered Horizontally, Top
                val mainX = (containerWidth - mainWidth) / 2
                mainPlaceable.placeRelative(mainX, 0)

                // Trailing: Centered Horizontally (or align start?), Bottom
                val trailingXCentered = (containerWidth - trailingWidth) / 2
                trailingPlaceable.placeRelative(trailingXCentered, mainPlaceable.height + spacingPx)
            }
        }
    }
}