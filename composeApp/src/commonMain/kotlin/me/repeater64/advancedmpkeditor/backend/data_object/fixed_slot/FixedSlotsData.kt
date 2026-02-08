package me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot

data class FixedSlotsData(
    val offhandSlotData: OffhandSlotData,
    val hotbarSlotsData: Array<HotbarSlotData>,
    val inventorySlotsData: Array<InventorySlotData>,
    val helmetSlotData: HelmetSlotData,
    val chestplateSlotData: ChestplateSlotData,
    val leggingsSlotData: LeggingsSlotData,
    val bootsSlotData: BootsSlotData
) {

    fun allSlots() : List<FixedSlotData> {
        return listOf(offhandSlotData, helmetSlotData, chestplateSlotData, leggingsSlotData, bootsSlotData) + hotbarSlotsData.toList() + inventorySlotsData.toList()
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