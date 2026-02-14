package me.repeater64.advancedmpkeditor.gui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteIconAndTooltip(onRemove: () -> Unit) = SmallIconAndTooltip(onRemove, "Delete", Icons.Default.Close)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallIconAndTooltip(onClick: () -> Unit, tooltipText: String, icon: ImageVector) {
    val tooltipState = rememberTooltipState()
    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
        tooltip = {
            PlainTooltip { Text(tooltipText) }
        },
        state = tooltipState
    ) {
        Icon(
            imageVector = icon,
            contentDescription = tooltipText,
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onClick)
                .background(color = androidx.compose.ui.graphics.Color.Transparent, shape = CircleShape)
                .padding(4.dp)
        )
    }
}