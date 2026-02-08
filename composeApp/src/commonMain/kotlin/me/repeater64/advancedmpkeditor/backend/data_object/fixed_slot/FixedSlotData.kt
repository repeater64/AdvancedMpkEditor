package me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot

import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList

abstract class FixedSlotData(val minecraftSlotID: String, open val itemOptions: WeightedOptionList<MinecraftItem>) {

    companion object {
        private val providers by lazy { listOf(HotbarSlotData, InventorySlotData, OffhandSlotData, HelmetSlotData, ChestplateSlotData, LeggingsSlotData, BootsSlotData) }


        fun createEmpty(slotString: String): FixedSlotData {
            val factory = providers.find { it.matches(slotString) }
                ?: throw IllegalArgumentException("Error loading FixedSlotData: No matching class for: $slotString")

            return factory.create(slotString)
        }
    }
}

interface FixedSlotDataFactory<out T : FixedSlotData> {
    val slotStringPatternToLookFor: String
    fun create(slotString: String): T

    fun matches(input: String): Boolean = input.contains(slotStringPatternToLookFor)

    fun empty(slotNum: Int = 0): T
}