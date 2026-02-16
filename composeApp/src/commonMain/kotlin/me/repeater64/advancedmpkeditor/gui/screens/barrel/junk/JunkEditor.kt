package me.repeater64.advancedmpkeditor.gui.screens.barrel.junk

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import me.repeater64.advancedmpkeditor.backend.data_object.junk.JunkSettings
import me.repeater64.advancedmpkeditor.gui.component.MinecraftSlotDisplay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun ColumnScope.JunkEditor(
    junkSettings: JunkSettings,
    showDialogCallback: (@Composable () -> Any) -> Any,
    hideDialogCallback: () -> Unit
) {
    Text("Junk Settings", style = MaterialTheme.typography.headlineLarge)
    Spacer(Modifier.height(15.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = junkSettings.enableJunk,
            onCheckedChange = {junkSettings.enableJunk = it},
            modifier = Modifier.size(20.dp),
        )
        Spacer(Modifier.width(6.dp))
        Text("Fill Inventory With Junk Items", style = MaterialTheme.typography.titleMedium)
    }

    Spacer(Modifier.height(15.dp))

    val tooltipState = rememberTooltipState()
    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
        tooltip = {
            if (junkSettings.enableJunk) {
                PlainTooltip { Text(text = "Click to edit!") }
            }
        },
        state = tooltipState,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(horizontal = 50.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = (-30).dp,
            shadowElevation = 4.dp,
            onClick = {
                showDialogCallback { JunkPopup(junkSettings, hideDialogCallback) }
            },
            enabled = junkSettings.enableJunk,
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text("Junk Items", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(start=20.dp, top=5.dp).alpha(if (junkSettings.enableJunk) 1f else 0.38f))

                BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                    val availableWidth = maxWidth.value
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(Modifier.width(18.dp))
                        var widthSoFar = 18f
                        for (item in junkSettings.junkList) {
                            widthSoFar += 50
                            if (widthSoFar >= availableWidth-30) {
                                Text(" ...", style = MaterialTheme.typography.bodyLarge)
                                return@Row
                            } else {
                                MinecraftSlotDisplay(item.option, 50).ContentsOnly(if (junkSettings.enableJunk) null else 0.38f)
                            }
                        }
                    }
                }
            }
        }
    }
}