package me.repeater64.advancedmpkeditor.gui.screens.barrel

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    inputBarrelItem: BarrelItem,
    barrelItemObjectChanged: (BarrelItem) -> Any,
    nameChanged: () -> Any
) {

    var barrelItem by remember { mutableStateOf(inputBarrelItem) }

    fun changeBarrelItemObject(newBarrelItem: BarrelItem) {
        barrelItem = newBarrelItem
        barrelItemObjectChanged(newBarrelItem)
    }


    Row {
        SimpleTextField(
            modifier=Modifier.width(300.dp),
            label="Barrel Name",
            currentText = barrelItem.name,
            textChanged = { changeBarrelItemObject(barrelItem.copy(name = it)); nameChanged() }
        )
        Spacer(Modifier.width(20.dp))
        SimpleDropdown(
            modifier=Modifier.width(300.dp),
            label="Select Practice Type",
            selectedValue = barrelItem.practiceTypeOption.displayName,
            options = PracticeTypeOption.entries.map { it.displayName },
            optionSelected = { changeBarrelItemObject(barrelItem.copy(practiceTypeOption = PracticeTypeOption.entries[it])) }
        )
        Spacer(Modifier.width(20.dp))
        SimpleDropdown(
            modifier=Modifier.width(200.dp),
            label="Select Gamemode",
            selectedValue = barrelItem.gamemodeOption.displayName,
            options = GamemodeOption.entries.map { it.displayName },
            optionSelected = { changeBarrelItemObject(barrelItem.copy(gamemodeOption = GamemodeOption.entries[it])) }
        )
        Spacer(Modifier.width(20.dp))
        SimpleDropdown(
            modifier=Modifier.width(200.dp),
            label="Select Difficulty",
            selectedValue = barrelItem.difficultyOption.displayName,
            options = DifficultyOption.entries.map { it.displayName },
            optionSelected = { changeBarrelItemObject(barrelItem.copy(difficultyOption = DifficultyOption.entries[it])) }
        )
    }

    Spacer(Modifier.height(50.dp))
    FixedSlotsEditor(inputBarrelItem.fixedSlotsData)
}