package me.repeater64.advancedmpkeditor.gui.screens.barrel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import me.repeater64.advancedmpkeditor.backend.data_object.item.ForcedEmptyMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItemCategory
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItemWithAmount
import me.repeater64.advancedmpkeditor.backend.data_object.item.SimpleMinecraftItem
import me.repeater64.advancedmpkeditor.gui.component.MinecraftSlotDisplay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MinecraftItemChooserPopup(
    onDismiss: () -> Unit,
    onItemChosen: (MinecraftItem) -> Unit,
    range: Boolean = false,
    onItemRangeChosen: (item: MinecraftItem, min: Int, max: Int, num: Int) -> Unit = {_, _, _, _ -> },
    allowMoreThanAStack: Boolean,
    initiallySelectedItem: MinecraftItem?,
    itemToAlwaysPutAtStart: () -> MinecraftItem? = { ForcedEmptyMinecraftItem() },
    onlyOneCategory: MinecraftItemCategory? = null,
) {
    var selectedCategory by remember { mutableStateOf(onlyOneCategory ?: MinecraftItemCategory.ALL) }
    val allCategories = if (onlyOneCategory != null) listOf(onlyOneCategory) else MinecraftItemCategory.entries.filterNot { it.isSpecificArmorSlotCategory }
    var searchText by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf(initiallySelectedItem?.amount?.toString() ?: "1") }
    var selectedItem by remember { mutableStateOf(initiallySelectedItem) }

    var minText by remember { mutableStateOf("10") }
    var maxText by remember { mutableStateOf("50") }
    var numOptionsText by remember { mutableStateOf("11") }

    fun getItems(): List<MinecraftItem> {
        if (range) {
            return (if (searchText.isBlank()) selectedCategory.items else selectedCategory.items.filter { it.displayName.contains(searchText, true) }).let { if (!allowMoreThanAStack) it.filter { item -> item.stackSize > 1 && item is MinecraftItemWithAmount} else it }
        } else {
            val itemAtStart = itemToAlwaysPutAtStart()
            return (itemAtStart?.let {listOf(it)} ?: emptyList()) + if (searchText.isBlank()) selectedCategory.items else selectedCategory.items.filter { it.displayName.contains(searchText, true) }
        }
    }

    fun trySetItemThenDismiss() {
        if (selectedItem == null) {
            onDismiss()
            return
        }

        val amount = if (allowMoreThanAStack) {
            (amountText.toIntOrNull() ?: 1)
        } else {
            (amountText.toIntOrNull() ?: 1).coerceAtMost(selectedItem!!.stackSize)
        }

        val finalItemToReturn = if (selectedItem is MinecraftItemWithAmount) (selectedItem as MinecraftItemWithAmount).copyWithAmount(amount) else selectedItem!!

        onItemChosen(finalItemToReturn)
        onDismiss()
    }

    fun trySetItemRangeThenDismiss() {
        if (selectedItem == null) {
            return
        }

        val min = if (allowMoreThanAStack) { (minText.toIntOrNull() ?: 1) } else { (minText.toIntOrNull() ?: 1).coerceAtMost(selectedItem!!.stackSize) }
        val max = (if (allowMoreThanAStack) { (maxText.toIntOrNull() ?: 1) } else { (maxText.toIntOrNull() ?: 1).coerceAtMost(selectedItem!!.stackSize) }).coerceAtLeast(min)
        val num = (numOptionsText.toIntOrNull() ?: 1).coerceAtLeast(1)

        onItemRangeChosen(selectedItem!!, min, max, num)
        onDismiss()
    }

    Popup(
        alignment = Alignment.TopCenter,
        offset = IntOffset(0, -100),
        onDismissRequest = { if (range) onDismiss() else trySetItemThenDismiss() },
        properties = PopupProperties(focusable = true)
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 4.dp,
            tonalElevation = 4.dp,
            modifier = Modifier
                .width(600.dp)
                .heightIn(max = 500.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (range) "Choose Item (Amount Range)" else "Choose Item",
                    style = MaterialTheme.typography.titleLarge,
                )

                // Category pills
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    allCategories.forEach { category ->
                        val isSelected = selectedCategory == category

                        OutlinedButton(
                            onClick = { selectedCategory = category },
                            shape = CircleShape,
                            border = BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(
                                text = category.displayName,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(bottom = 1.dp)
                            )
                        }
                    }
                }

                // Search Bar
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search items...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )

                HorizontalDivider()

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 40.dp),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(getItems()) { item ->
                        MinecraftSlotDisplay(
                            item,
                            40,
                            tooltipContents = {Text(item.displayName)},
                            highlighted = selectedItem?.equalsIgnoringAmount(item) ?: false,
                            modifier = Modifier.onClick(onClick = {
                                selectedItem = item
                                if (range) {
                                    if (minText.isNotBlank() && !allowMoreThanAStack && minText.toInt() > item.stackSize) { minText = item.stackSize.toString() }
                                    if (maxText.isNotBlank() && !allowMoreThanAStack && maxText.toInt() > item.stackSize) { maxText = item.stackSize.toString() }
                                } else {
                                    if (amountText.isNotBlank() && !allowMoreThanAStack && amountText.toInt() > item.stackSize) {
                                        amountText = item.stackSize.toString()
                                    }
                                }
                            })
                        ).SlotDisplay()
                    }

                }

                HorizontalDivider()

                // Footer (Amount and ok button)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = if (range) Arrangement.Start else Arrangement.SpaceBetween
                ) {
                    if (range) {
                        // Input Fields
                        NumberInputField("Min", 80, minText, {minText = it}, if (allowMoreThanAStack) 1000 else if (selectedItem == null) 0 else selectedItem!!.stackSize)
                        Spacer(Modifier.width(20.dp))
                        NumberInputField("Max", 80, maxText, {maxText = it}, if (allowMoreThanAStack) 1000 else if (selectedItem == null) 0 else selectedItem!!.stackSize)
                        Spacer(Modifier.width(20.dp))
                        NumberInputField("Num Options", 130, numOptionsText, {numOptionsText = it}, 32)

                        Spacer(Modifier.weight(1f))

                        // Ok Button
                        Button(
                            onClick = {
                                trySetItemRangeThenDismiss()
                            }
                        ) {
                            Text("Add")
                        }
                    } else {
                        // Amount Input
                        NumberInputField("Amount", 120, amountText, {amountText = it}, if (allowMoreThanAStack) 1000 else if (selectedItem == null) 0 else selectedItem!!.stackSize)

                        // Ok Button
                        Button(
                            onClick = {
                                trySetItemThenDismiss()
                            }
                        ) {
                            Text("Choose Item")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NumberInputField(label: String, width: Int, numText: String, numTextSetter: (String) -> Unit, maxValue: Int) {
    OutlinedTextField(
        value = numText,
        onValueChange = { input ->
            // Validation: Only allow digits
            if (input.all { it.isDigit() }) {
                if (input.isEmpty()) numTextSetter(input) // Allow deleting all characters to type more
                else if (input.toInt() <= maxValue) {
                    numTextSetter(input)
                }
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = Modifier.width(width.dp)
    )
}