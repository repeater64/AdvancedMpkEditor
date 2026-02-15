package me.repeater64.advancedmpkeditor.gui.screens.barrel

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.DifficultyOption
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.GamemodeOption
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.PracticeTypeOption
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.BarrelItem
import me.repeater64.advancedmpkeditor.gui.component.CenteredRowWithOverflow
import me.repeater64.advancedmpkeditor.gui.component.SimpleDropdown
import me.repeater64.advancedmpkeditor.gui.component.SimpleTextField
import me.repeater64.advancedmpkeditor.gui.screens.barrel.fire_res.FireResEditor
import me.repeater64.advancedmpkeditor.gui.screens.barrel.fixed_slot.FixedSlotsEditor
import me.repeater64.advancedmpkeditor.gui.screens.barrel.fixed_slot.InventorySlotKey
import me.repeater64.advancedmpkeditor.gui.screens.barrel.health_hunger.HealthHungerEditor
import me.repeater64.advancedmpkeditor.gui.screens.barrel.random_slot.RandomSlotsEditor

@Composable
fun ColumnScope.BarrelEditor(
    barrelItem: BarrelItem,
    showDialogCallback: (@Composable () -> Any) -> Any,
    hideDialogCallback: () -> Unit
) {


    FlowRow {
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


    CenteredRowWithOverflow(
        modifier = Modifier.fillMaxWidth(),
        spacing = 0.dp,
        mainContent = {
            FixedSlotsEditor(barrelItem.fixedSlotsData, barrelItem.allRandomiserLinkLabels, showDialogCallback, hideDialogCallback)
        },
        trailingContent = {
            // Sits to the right of FixedSlotsEditor if space, or if not, below it
            InventorySlotKey()
        }
    )

    // Check if we don't have enough empty slots
    if (barrelItem.fixedSlotsData.numAvailableForRandomInvSlots() < barrelItem.randomSlotsData.totalMaxNumStacks()) {
        Spacer(Modifier.height(25.dp))
        Text("Warning - you don't have enough available slots! The maximum number of random items you could end up with is ${barrelItem.randomSlotsData.totalMaxNumStacks()}, and only ${barrelItem.fixedSlotsData.numAvailableForRandomInvSlots()} slots are marked as \"Available for Random Items\" in your inventory! Because of the way MPK works, this means you'll end up not receiving some of your random items every single time. Please clear out some more slots or reduce the maximum possible number of random items!",
            style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center, modifier = Modifier.width(800.dp))
        Spacer(Modifier.height(25.dp))
    } else {
        Spacer(Modifier.height(50.dp))
    }


    RandomSlotsEditor(barrelItem.randomSlotsData, barrelItem.allRandomiserLinkLabels, showDialogCallback, hideDialogCallback)
    Spacer(Modifier.height(50.dp))
    HealthHungerEditor(barrelItem.healthHungerSettings, barrelItem.allRandomiserLinkLabels, showDialogCallback, hideDialogCallback)
    Spacer(Modifier.height(50.dp))
    FireResEditor(barrelItem.fireResSettings, barrelItem.allRandomiserLinkLabels, showDialogCallback, hideDialogCallback)
}