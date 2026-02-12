package me.repeater64.advancedmpkeditor.gui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipScope
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem

@OptIn(ExperimentalMaterial3Api::class)
data class MinecraftSlotDisplay(val minecraftItem: MinecraftItem,
                                val size: Int,
                                val modifier: Modifier = Modifier,
                                val tooltipContents: @Composable TooltipScope.() -> Unit = {},
                                val highlighted: Boolean = false
    ) {
    @Composable
    fun SlotDisplay(showContents: Boolean = true) {
        Surface(
            modifier = modifier.size(size.dp),
            shape = RoundedCornerShape((size / 6.0f).dp),
        border = if (highlighted) BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null
        ) {
            val tooltipState = rememberTooltipState()

            Box(contentAlignment = Alignment.Center) { TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                tooltip = {
                    PlainTooltip() {tooltipContents()}
                },
                state = tooltipState,
            ) {
                if (showContents) {
                    ContentsOnly()
                }
            }}
        }
    }

    @Composable
    fun ContentsOnly() {
        Box(contentAlignment = Alignment.Center) {
            MinecraftItemIcon(
                minecraftItem, modifier = Modifier.size((size * 0.85).dp)
            )
        }
    }
}