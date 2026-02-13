package me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import me.repeater64.advancedmpkeditor.backend.data_object.ContentHashable
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable.Companion.getListAndRemainingPages
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable.Companion.getObjectAndRemainingPages
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable.Companion.serializeList
import me.repeater64.advancedmpkeditor.util.hash

@Stable
class FixedSlotsData(
    _offhandSlotData: OffhandSlotData,
    _hotbarSlotsData: List<HotbarSlotData>,
    _inventorySlotsData: List<InventorySlotData>,
    _helmetSlotData: HelmetSlotData,
    _chestplateSlotData: ChestplateSlotData,
    _leggingsSlotData: LeggingsSlotData,
    _bootsSlotData: BootsSlotData
) : ContentHashable {

    val offhandSlotData by mutableStateOf(_offhandSlotData)
    val hotbarSlotsData = _hotbarSlotsData.toMutableStateList()
    val inventorySlotsData = _inventorySlotsData.toMutableStateList()
    val helmetSlotData by mutableStateOf(_helmetSlotData)
    val chestplateSlotData by mutableStateOf(_chestplateSlotData)
    val leggingsSlotData by mutableStateOf(_leggingsSlotData)
    val bootsSlotData by mutableStateOf(_bootsSlotData)

    override fun contentHash(): Int {
        return hash(
            offhandSlotData.contentHash(),
            hotbarSlotsData.map { it.contentHash() },
            inventorySlotsData.map {it.contentHash() },
            helmetSlotData.contentHash(),
            chestplateSlotData.contentHash(),
            leggingsSlotData.contentHash(),
            bootsSlotData.contentHash(),
        )
    }

    fun getAllSlotsExceptFloatingInvSlots() : List<FixedSlotData> {
        return listOf(offhandSlotData, helmetSlotData, chestplateSlotData, leggingsSlotData, bootsSlotData) + hotbarSlotsData.toList() + inventorySlotsData.toList().filter { !it.isAvailableForRandomItems() && it.inventoryPosition < numInvSlotsWithItems()}
    }

    fun getNonFloatingInvSlotInventoryPositions(): Set<Int> {
        return inventorySlotsData.toList().mapIndexedNotNull { index, it -> if (!it.isAvailableForRandomItems() && it.inventoryPosition < numInvSlotsWithItems()) index else null }.toSet()
    }

    fun getFloatingInvSlots() : List<InventorySlotData> {
        return inventorySlotsData.toList().filter { !it.isAvailableForRandomItems() && (it.inventoryPosition < numInvSlotsWithItems()).not() }
    }

    fun numInvSlotsWithItems(): Int {
        return inventorySlotsData.count { !it.isAvailableForRandomItems() }
    }

    fun numAvailableForRandomInvSlots(): Int {
        return inventorySlotsData.count { it.isAvailableForRandomItems() }
    }

    companion object : BookSerializable<FixedSlotsData> {
        override val className = "FixedSlotsData"

        override fun serializeToPages(it: FixedSlotsData): List<String> {
            return OffhandSlotData.serialize(it.offhandSlotData) +
                    serializeList(it.hotbarSlotsData.toList(), HotbarSlotData) +
                    serializeList(it.inventorySlotsData.toList(), InventorySlotData) +
                    HelmetSlotData.serialize(it.helmetSlotData) +
                    ChestplateSlotData.serialize(it.chestplateSlotData) +
                    LeggingsSlotData.serialize(it.leggingsSlotData) +
                    BootsSlotData.serialize(it.bootsSlotData)
        }

        override fun deserializeFromPages(pages: List<String>): FixedSlotsData {
            val (offhandSlotData, pages1) = getObjectAndRemainingPages(pages, OffhandSlotData)
            val (hotbarSlotsData, pages2) = getListAndRemainingPages(pages1, HotbarSlotData)
            val (inventorySlotsData, pages3) = getListAndRemainingPages(pages2, InventorySlotData)
            val (helmetSlotData, pages4) = getObjectAndRemainingPages(pages3, HelmetSlotData)
            val (chestplateSlotData, pages5) = getObjectAndRemainingPages(pages4, ChestplateSlotData)
            val (leggingsSlotData, pages6) = getObjectAndRemainingPages(pages5, LeggingsSlotData)
            val bootsSlotData = getObjectAndRemainingPages(pages6, BootsSlotData).first
            return FixedSlotsData(offhandSlotData, hotbarSlotsData, inventorySlotsData, helmetSlotData, chestplateSlotData, leggingsSlotData, bootsSlotData)
        }
    }
}