package me.repeater64.advancedmpkeditor.gui.screens.barrel.junk

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.repeater64.advancedmpkeditor.backend.data_object.item.ForcedEmptyMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.junk.JunkSettings
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionEitherType
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionNoLinks
import me.repeater64.advancedmpkeditor.backend.presets_examples.presets.JunkPreset
import me.repeater64.advancedmpkeditor.gui.component.MinecraftSlotDisplayMultiImpl
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
        toAddWhenOnlyOptionRemoved = { WeightedOptionNoLinks(ForcedEmptyMinecraftItem()) },
        addNewRowClicked = {
            junkSettings.junkList.add(WeightedOptionNoLinks(ForcedEmptyMinecraftItem()))
        },
        showAddPresetButton = { true },
        addPresetText = "Add Items from Preset",
        presetDropdownContents = {
            for (preset in JunkPreset.entries) {
                DropdownMenuItem(
                    text = { Text(preset.displayName) },
                    leadingIcon = {
                        MinecraftSlotDisplayMultiImpl(
                            preset.options.map { it.option },
                            size = 50
                        ).ContentsOnly()
                    },
                    onClick = {
                        if (junkSettings.junkList.size == 1 && junkSettings.junkList[0].option is ForcedEmptyMinecraftItem) {
                            junkSettings.junkList.clear()
                        }
                        junkSettings.junkList.addAll(preset.optionsGetter())
                    }
                )
            }
        },
        footerLeftSideContent = {
            Button(
                onClick = {
                    junkSettings.junkList.clear()
                    junkSettings.junkList.add(WeightedOptionNoLinks(ForcedEmptyMinecraftItem()))
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Clear All")
            }
        },
        leftColumnContent = { weightedOption ->
            SimpleSingleItemChooser(weightedOption, null)
        }
    )
}