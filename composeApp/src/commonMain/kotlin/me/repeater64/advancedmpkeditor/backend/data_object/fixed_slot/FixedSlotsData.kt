package me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable.Companion.getListAndRemainingPages
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable.Companion.getObjectAndRemainingPages
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable.Companion.serializeList

data class FixedSlotsData(
    val offhandSlotData: OffhandSlotData,
    val hotbarSlotsData: Array<HotbarSlotData>,
    val inventorySlotsData: Array<InventorySlotData>,
    val helmetSlotData: HelmetSlotData,
    val chestplateSlotData: ChestplateSlotData,
    val leggingsSlotData: LeggingsSlotData,
    val bootsSlotData: BootsSlotData
) {

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
            return FixedSlotsData(offhandSlotData, hotbarSlotsData.toTypedArray(), inventorySlotsData.toTypedArray(), helmetSlotData, chestplateSlotData, leggingsSlotData, bootsSlotData)
        }

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as FixedSlotsData

        if (offhandSlotData != other.offhandSlotData) return false
        if (!hotbarSlotsData.contentEquals(other.hotbarSlotsData)) return false
        if (!inventorySlotsData.contentEquals(other.inventorySlotsData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = offhandSlotData.hashCode()
        result = 31 * result + hotbarSlotsData.contentHashCode()
        result = 31 * result + inventorySlotsData.contentHashCode()
        return result
    }
}