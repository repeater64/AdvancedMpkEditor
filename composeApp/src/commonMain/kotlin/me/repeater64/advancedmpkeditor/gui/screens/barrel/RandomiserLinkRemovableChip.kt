package me.repeater64.advancedmpkeditor.gui.screens.barrel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.RandomiserCondition
import me.repeater64.advancedmpkeditor.gui.component.DeleteIconAndTooltip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomiserLinkRemovableChip(condition: RandomiserCondition, onRemove: () -> Unit, modifier: Modifier = Modifier) = RandomiserLinkRemovableChip(condition.conditionLabel, condition.isInverted, false, onRemove, modifier)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomiserLinkRemovableChip(triggerLabel: String, onRemove: () -> Unit, modifier: Modifier = Modifier) = RandomiserLinkRemovableChip(triggerLabel, false, true, onRemove, modifier)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomiserLinkRemovableChip(
    label: String,
    isInverted: Boolean,
    isTrigger: Boolean,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) = Surface(
    modifier = modifier.padding(top = 2.dp, bottom = 2.dp),
    shape = CircleShape,
    color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 12.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
    ) {
        Text(
            text = if (isTrigger) "Triggers: $label" else if (isInverted) "Unless: $label" else "Only If: $label",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(end = 8.dp)
        )

        // Delete icon + tooltip
        DeleteIconAndTooltip(onRemove)
    }
}