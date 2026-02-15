package me.repeater64.advancedmpkeditor.gui.screens.barrel

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.onClick
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.FixedSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.FixedSlotsData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.InventorySlotData
import me.repeater64.advancedmpkeditor.backend.data_object.item.DontReplaceMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.ForcedEmptyMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItemCategory
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.gui.component.DragDropContainer
import me.repeater64.advancedmpkeditor.gui.component.DragSwappable
import me.repeater64.advancedmpkeditor.gui.component.MinecraftSlotDisplay
import me.repeater64.advancedmpkeditor.gui.component.MinecraftSlotDisplayMulti
import me.repeater64.advancedmpkeditor.gui.component.UpdateCounterWrapper
import kotlin.math.roundToInt

const val SLOT_SIZE = 75

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColumnScope.FixedSlotsEditor(
    fixedSlotsData: FixedSlotsData,
    allLabels: MutableSet<String>,
    showDialogCallback: (@Composable () -> Any) -> Any,
    hideDialogCallback: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text("Fixed Slot Items", style = MaterialTheme.typography.headlineLarge)
    Spacer(Modifier.height(15.dp))

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row {
            ArmorSlotDisplay(fixedSlotsData.helmetSlotData, "empty_helmet_slot", MinecraftItemCategory.ALL_HELMETS, allLabels, showDialogCallback, hideDialogCallback)
            Spacer(Modifier.width(10.dp))
            ArmorSlotDisplay(fixedSlotsData.chestplateSlotData, "empty_chestplate_slot", MinecraftItemCategory.ALL_CHESTPLATES, allLabels, showDialogCallback, hideDialogCallback)
            Spacer(Modifier.width(10.dp))
            ArmorSlotDisplay(fixedSlotsData.leggingsSlotData, "empty_leggings_slot", MinecraftItemCategory.ALL_LEGGINGS, allLabels, showDialogCallback, hideDialogCallback)
            Spacer(Modifier.width(10.dp))
            ArmorSlotDisplay(fixedSlotsData.bootsSlotData, "empty_boots_slot", MinecraftItemCategory.ALL_BOOTS, allLabels, showDialogCallback, hideDialogCallback)
        }
        Spacer(Modifier.height(15.dp))

        // Inventory and hotbar and offhand
        // For the DragDropContainer, indexed by 0-8 is hotbar, 9+ is inv, 36 is offhand
        DragDropContainer(contentAlignment = Alignment.Center,
            onSwap = { srcKey, destKey ->
                val srcSlot = if (srcKey == 36) fixedSlotsData.offhandSlotData else if (srcKey < 9) fixedSlotsData.hotbarSlotsData[srcKey] else fixedSlotsData.inventorySlotsData[srcKey-9]
                val destSlot = if (destKey == 36) fixedSlotsData.offhandSlotData else if (destKey < 9) fixedSlotsData.hotbarSlotsData[destKey] else fixedSlotsData.inventorySlotsData[destKey-9]

                var srcItems = srcSlot.itemOptions
                var destItems = destSlot.itemOptions

                if (srcSlot is InventorySlotData && destSlot !is InventorySlotData && srcItems.options.size == 1 && srcItems.options[0].option is DontReplaceMinecraftItem) {
                    // Moving an "available for random items" slot out of the inventory, convert it to forced empty slot
                    srcItems = WeightedOptionList(mutableListOf(WeightedOption(ForcedEmptyMinecraftItem(), 1)))
                } else if (destSlot is InventorySlotData && srcSlot !is InventorySlotData && destItems.options.size == 1 && destItems.options[0].option is DontReplaceMinecraftItem) {
                    // Same story
                    destItems = WeightedOptionList(mutableListOf(WeightedOption(ForcedEmptyMinecraftItem(), 1)))
                }

                srcSlot.itemOptions = destItems
                destSlot.itemOptions = srcItems

            },
            emptyChecker = { key ->
                val slot = if (key == 36) fixedSlotsData.offhandSlotData else if (key < 9) fixedSlotsData.hotbarSlotsData[key] else fixedSlotsData.inventorySlotsData[key-9]

                (slot is InventorySlotData && slot.itemOptions.options.size == 1 && slot.itemOptions.options[0].option is DontReplaceMinecraftItem)
            }
        ) {Column{
            for (row in 0 until 3) {
                Row {
                    Spacer(Modifier.width((SLOT_SIZE + 10).dp)) // To make up for the offhand slot
                    for (col in 0 until 9) {
                        Spacer(Modifier.width(2.dp))

                        val invPosition = row*9 + col
                        val slotData = fixedSlotsData.inventorySlotsData[invPosition]
                        FixedSlotDisplay(slotData, allLabels, invPosition+9, showDialogCallback, hideDialogCallback)

                        Spacer(Modifier.width(2.dp))
                    }
                    Spacer(Modifier.width((SLOT_SIZE + 10).dp)) // Equivalent to the offhand slot again, to force the centering we want
                }
                Spacer(Modifier.height(4.dp))
            }

            // Space between inv and hotbar
            Spacer(Modifier.height(8.dp))

            Row {
                // Offhand
                val offhandSlotData = fixedSlotsData.offhandSlotData
                FixedSlotDisplay(offhandSlotData, allLabels, 36, showDialogCallback, hideDialogCallback)

                Spacer(Modifier.width(10.dp))

                // Hotbar
                for (hotbarSlot in 0 until 9) {
                    Spacer(Modifier.width(2.dp))

                    val slotData = fixedSlotsData.hotbarSlotsData[hotbarSlot]
                    FixedSlotDisplay(slotData, allLabels, hotbarSlot, showDialogCallback, hideDialogCallback)

                    Spacer(Modifier.width(2.dp))
                }
                Spacer(Modifier.width((SLOT_SIZE + 10).dp)) // Equivalent to the offhand slot again, to force the centering we want
            }
        }}
    }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RowScope.FixedSlotDisplay(slotData: FixedSlotData, allLabels: MutableSet<String>, dragSwappableKey: Int, showDialogCallback: (@Composable () -> Any) -> Any, hideDialogCallback: () -> Unit) {
    val minecraftSlotDisplay = MinecraftSlotDisplayMulti(
        options = slotData.itemOptions,
        size = SLOT_SIZE,
        modifier = Modifier.onClick(onClick = {
            showDialogCallback { FixedSlotPopup(slotData, allLabels, hideDialogCallback) }
        }),
        displayDontReplaceAsAir = true
    )

    DragSwappable(
        key = dragSwappableKey,
        ghostContent = { minecraftSlotDisplay.ContentsOnly() },
        content = { isDragging -> minecraftSlotDisplay.SlotDisplay(!isDragging) }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RowScope.ArmorSlotDisplay(slotData: FixedSlotData, ifEmpty: String, itemCategory: MinecraftItemCategory, allLabels: MutableSet<String>, showDialogCallback: (@Composable () -> Any) -> Any, hideDialogCallback: () -> Unit) {
    val minecraftSlotDisplay = MinecraftSlotDisplayMulti(
        options = slotData.itemOptions,
        size = SLOT_SIZE,
        ifEmpty = ifEmpty,
        tooltipContents = {Text("Click to edit items", style = MaterialTheme.typography.bodyMedium)},
        modifier = Modifier.onClick(onClick = {
            showDialogCallback { FixedSlotPopup(slotData, allLabels, hideDialogCallback, onlyOneItemCategory = itemCategory) }
        }),
        displayDontReplaceAsAir = true
    )

    minecraftSlotDisplay.SlotDisplay()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventorySlotKey() {
    Column(horizontalAlignment = Alignment.Start) {

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 40.dp, bottom = 10.dp)) {
            MinecraftSlotDisplay(
                minecraftItem = DontReplaceMinecraftItem(),
                size = (SLOT_SIZE*0.6).roundToInt(),
                tooltipContents = { Text("Example Available for Random Items Slot") },
                displayDontReplaceAsAir = true
            ).SlotDisplay()

            Spacer(Modifier.width(8.dp))

            Text("Available for Random Items", style = MaterialTheme.typography.titleMedium)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            MinecraftSlotDisplay(
                minecraftItem = ForcedEmptyMinecraftItem(),
                size = (SLOT_SIZE*0.6).roundToInt(),
                tooltipContents = { Text("Example Empty Slot") },
            ).SlotDisplay()

            Spacer(Modifier.width(8.dp))

            Text("Empty Slot", style = MaterialTheme.typography.titleMedium)
        }
    }
}