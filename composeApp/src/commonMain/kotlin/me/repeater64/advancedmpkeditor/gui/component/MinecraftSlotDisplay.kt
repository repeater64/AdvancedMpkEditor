package me.repeater64.advancedmpkeditor.gui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipScope
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.repeater64.advancedmpkeditor.backend.data_object.item.DontReplaceMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.ForcedEmptyMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.SimpleMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList

@OptIn(ExperimentalMaterial3Api::class)
class MinecraftSlotDisplay(minecraftItem: MinecraftItem,
                                size: Int,
                                modifier: Modifier = Modifier,
                                tooltipContents: @Composable TooltipScope.() -> Unit = {},
                                highlighted: Boolean = false,
                                ifEmpty: String? = null,
                                displayDontReplaceAsAir: Boolean = false,
    )
    : MinecraftSlotDisplayMulti(WeightedOptionList(mutableListOf(WeightedOption(minecraftItem, 1))), size, modifier, tooltipContents, highlighted, ifEmpty, displayDontReplaceAsAir)

@OptIn(ExperimentalMaterial3Api::class)
open class MinecraftSlotDisplayMulti(val options: WeightedOptionList<MinecraftItem>,
                                     val size: Int,
                                     val modifier: Modifier = Modifier,
                                     val tooltipContents: @Composable TooltipScope.() -> Unit = {
                                         Column{Text("Click to edit items", style = MaterialTheme.typography.bodyMedium);Spacer(Modifier.height(2.dp));Text("Click and drag to swap slots", style = MaterialTheme.typography.bodyMedium)}},
                                     val highlighted: Boolean = false,
                                     val ifEmpty: String? = null,
                                     val displayDontReplaceAsAir: Boolean = false
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
    fun ContentsOnly(alpha: Float? = null) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size.dp)) {
            val itemOptions = options.optionsSortedByWeight
            if (itemOptions.isEmpty()) return@Box // Should be impossible anyway

            if (itemOptions.size == 1 && (itemOptions[0].option is DontReplaceMinecraftItem || (itemOptions[0].option is ForcedEmptyMinecraftItem && ifEmpty != null))) {
                // Slot is fully empty
                if (ifEmpty != null) {
                    MinecraftItemIcon(
                        SimpleMinecraftItem(ifEmpty, 1), modifier = Modifier.size((size * 0.85).dp), displayDontReplaceAsAir, alpha
                    )
                } else if (!displayDontReplaceAsAir) {
                    MinecraftItemIcon(
                        itemOptions[0].option, modifier = Modifier.size((size * 0.85).dp), alphaValue = alpha
                    )
                }
            } else {
                val halfSize = size * (0.85 / 2)
                if (itemOptions.size == 1) {
                    // Single item in slot
                    MinecraftItemIcon(
                        itemOptions[0].option, modifier = Modifier.size((size * 0.85).dp), displayDontReplaceAsAir, alphaValue = alpha
                    )
                } else if (itemOptions.size < 5) {
                    // Can render all items in sub-grid
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            GridCell(halfSize.dp) { MinecraftItemIcon(itemOptions[0].option, modifier = Modifier.size(halfSize.dp), displayDontReplaceAsAir, alphaValue = alpha) }
                            GridCell(halfSize.dp) { MinecraftItemIcon(if (itemOptions.size > 1) itemOptions[1].option else SimpleMinecraftItem("air", 1), modifier = Modifier.size(halfSize.dp), displayDontReplaceAsAir, alphaValue = alpha) }
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            GridCell(halfSize.dp) { MinecraftItemIcon(if (itemOptions.size > 2) itemOptions[2].option else SimpleMinecraftItem("air", 1), modifier = Modifier.size(halfSize.dp), displayDontReplaceAsAir, alphaValue = alpha) }
                            GridCell(halfSize.dp) { MinecraftItemIcon(if (itemOptions.size > 3) itemOptions[3].option else SimpleMinecraftItem("air", 1), modifier = Modifier.size(halfSize.dp), displayDontReplaceAsAir, alphaValue = alpha) }
                        }
                    }
                } else {
                    // Can render first 3 items in sub-grid, then ...
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            GridCell(halfSize.dp) { MinecraftItemIcon(itemOptions[0].option, modifier = Modifier.size(halfSize.dp), displayDontReplaceAsAir, alphaValue = alpha) }
                            GridCell(halfSize.dp) { MinecraftItemIcon(itemOptions[1].option, modifier = Modifier.size(halfSize.dp), displayDontReplaceAsAir, alphaValue = alpha) }
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            GridCell(halfSize.dp) { MinecraftItemIcon(itemOptions[2].option, modifier = Modifier.size(halfSize.dp), displayDontReplaceAsAir, alphaValue = alpha) }
                            GridCell(halfSize.dp) { Text("+${itemOptions.size-3}", style=MaterialTheme.typography.bodySmall) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GridCell(size: Dp, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}