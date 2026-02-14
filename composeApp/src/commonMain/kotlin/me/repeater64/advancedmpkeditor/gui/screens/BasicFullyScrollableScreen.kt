package me.repeater64.advancedmpkeditor.gui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.repeater64.advancedmpkeditor.gui.component.verticalColumnScrollbar

@Composable
fun BasicFullyScrollableScreen(modifier: Modifier = Modifier, verticalArrangement: Arrangement.Vertical = Arrangement.Top, content: @Composable ColumnScope.() -> Unit) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .padding(10.dp).verticalColumnScrollbar(scrollState).verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = verticalArrangement
    ) {
        content()
    }
}