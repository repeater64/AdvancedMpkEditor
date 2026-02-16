package me.repeater64.advancedmpkeditor.gui.screens.barrel.junk

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.repeater64.advancedmpkeditor.backend.data_object.fire_res.FireResSettings
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HealthHungerOption
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HealthOption
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HungerOption
import me.repeater64.advancedmpkeditor.backend.data_object.item.DontReplaceMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.junk.JunkSettings
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionEitherType
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionNoLinks
import me.repeater64.advancedmpkeditor.gui.component.MinecraftSlotDisplay
import me.repeater64.advancedmpkeditor.gui.screens.barrel.MinecraftItemChooserPopup
import me.repeater64.advancedmpkeditor.gui.screens.barrel.WeightedOptionListPopup
import me.repeater64.advancedmpkeditor.gui.screens.barrel.fixed_slot.SimpleSingleItemChooser

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun JunkPopup(
    junkSettings: JunkSettings,
    closePopupInputCallback: () -> Unit
) {
    WeightedOptionListPopup(
        junkSettings.junkList as SnapshotStateList<WeightedOptionEitherType<MinecraftItem>>, mutableSetOf(), closePopupInputCallback,

        width = 300,
        col1Weight = 0.3f,
        col2Weight = 0.2f,
        hasRandomiserLinks = false,

        topContent = {
            Text(
                text = "Junk Items List",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        firstColumnHeading = "Item",
        typeOfThing = "item",
        toAddWhenOnlyOptionRemoved = { WeightedOptionNoLinks(DontReplaceMinecraftItem(true)) },
        addNewRowClicked = {
            junkSettings.junkList.add(WeightedOptionNoLinks(DontReplaceMinecraftItem(true)))
        },
        showAddPresetButton = { false },
        presetDropdownContents = {},
        footerLeftSideContent = {},
        leftColumnContent = { weightedOption ->
            SimpleSingleItemChooser(weightedOption, null) { null }
        }
    )
}