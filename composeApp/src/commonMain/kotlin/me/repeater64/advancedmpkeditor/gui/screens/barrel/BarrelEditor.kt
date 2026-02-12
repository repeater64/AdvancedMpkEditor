package me.repeater64.advancedmpkeditor.gui.screens.barrel

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.DifficultyOption
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.GamemodeOption
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.PracticeTypeOption
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.BarrelItem
import me.repeater64.advancedmpkeditor.gui.component.SimpleDropdown
import me.repeater64.advancedmpkeditor.gui.component.SimpleTextField

@Composable
fun ColumnScope.BarrelEditor(
    barrelItem: BarrelItem
) {


    Row {
        SimpleTextField(
            modifier=Modifier.width(300.dp),
            label="Barrel Name",
            currentText = barrelItem.name,
            textChanged = { barrelItem.name = it }
        )
        Spacer(Modifier.width(20.dp))
        SimpleDropdown(
            modifier=Modifier.width(300.dp),
            label="Select Practice Type",
            selectedValue = barrelItem.practiceTypeOption.displayName,
            options = PracticeTypeOption.entries.map { it.displayName },
            optionSelected = { barrelItem.practiceTypeOption = PracticeTypeOption.entries[it] }
        )
        Spacer(Modifier.width(20.dp))
        SimpleDropdown(
            modifier=Modifier.width(200.dp),
            label="Select Gamemode",
            selectedValue = barrelItem.gamemodeOption.displayName,
            options = GamemodeOption.entries.map { it.displayName },
            optionSelected = { barrelItem.gamemodeOption = GamemodeOption.entries[it] }
        )
        Spacer(Modifier.width(20.dp))
        SimpleDropdown(
            modifier=Modifier.width(200.dp),
            label="Select Difficulty",
            selectedValue = barrelItem.difficultyOption.displayName,
            options = DifficultyOption.entries.map { it.displayName },
            optionSelected = { barrelItem.difficultyOption = DifficultyOption.entries[it] }
        )
    }

    Spacer(Modifier.height(50.dp))
    FixedSlotsEditor(barrelItem.fixedSlotsData)
}