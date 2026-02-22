package me.repeater64.advancedmpkeditor.backend.presets_examples

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.HotbarSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.InventorySlotData
import me.repeater64.advancedmpkeditor.backend.data_object.item.DontReplaceMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.ForcedEmptyMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.SimpleMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.RandomiserCondition
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.AirItem
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbar
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.SavedHotbarItem

fun item(id: String, weight: Int = 1, amount: Int = 1, label: String? = null, conditions: List<RandomiserCondition> = emptyList()): WeightedOption<MinecraftItem> {
    return WeightedOption(SimpleMinecraftItem(id, amount), weight, label, conditions)
}
fun rawItem(id: String, amount: Int = 1) = SimpleMinecraftItem(id, amount)
fun condition(label: String) = listOf(RandomiserCondition(label, false))
fun invCondition(label: String) = listOf(RandomiserCondition(label, true))
fun hotbarSlot(pos: Int, items: WeightedOptionList<MinecraftItem>) = HotbarSlotData(pos, items)
fun invSlot(pos: Int, items: WeightedOptionList<MinecraftItem>) = InventorySlotData(pos, items)
fun emptyItem(weight: Int = 1, label: String? = null, conditions: List<RandomiserCondition> = emptyList() ) = WeightedOption<MinecraftItem>(ForcedEmptyMinecraftItem(), weight, label, conditions)
fun availableItem(weight: Int = 1, label: String? = null, conditions: List<RandomiserCondition> = emptyList() ) = WeightedOption<MinecraftItem>(DontReplaceMinecraftItem(), weight, label, conditions)
fun optionList(vararg options: WeightedOption<MinecraftItem>) = WeightedOptionList(options.toMutableList())
fun itemList(vararg items: MinecraftItem, weight: Int = 1, label: String? = null, conditions: List<RandomiserCondition> = emptyList() ) = WeightedOption(items.toList().toMutableStateList(), weight, label, conditions)
fun emptyHotbar() = SavedHotbar(List(9) { AirItem() })
fun explosives(total: Int, anchors: Int = 0, weight: Int = 1) : WeightedOption<SnapshotStateList<MinecraftItem>> {
    val list = mutableListOf<MinecraftItem>()
    val beds = total-anchors
    if (beds > 0) {
        list.add(rawItem("white_bed", beds))
    }
    if (anchors > 0) {
        list.add(rawItem("respawn_anchor", anchors))
        list.add(rawItem("glowstone", anchors))
    }
    return WeightedOption(list.toMutableStateList(), weight)
}

fun explosiveIngredients(total: Int, anchors: Int = 0, weight: Int = 1) : WeightedOption<SnapshotStateList<MinecraftItem>> {
    val list = mutableListOf<MinecraftItem>()
    val beds = total-anchors
    if (beds > 0) {
        list.add(rawItem("string", beds*12 + (0..11).random()))
    }
    if (anchors > 0) {
        list.add(rawItem("crying_obsidian", anchors*6 + (0..20).random()))
        list.add(rawItem("glowstone_dust", anchors*16 + (0..15).random()))
    }
    return WeightedOption(list.toMutableStateList(), weight)
}

fun randomString(totalBeds: Int, weight: Int = 1) : WeightedOption<SnapshotStateList<MinecraftItem>> {
    return WeightedOption(listOf(rawItem("string", totalBeds*12 + (0..11).random())).toMutableStateList(), weight)
}