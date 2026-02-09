package me.repeater64.advancedmpkeditor.backend.data_object.trigger_items

import net.benwoodworth.knbt.NbtCompound
import net.benwoodworth.knbt.addNbtCompound
import net.benwoodworth.knbt.buildNbtCompound
import net.benwoodworth.knbt.put
import net.benwoodworth.knbt.putNbtCompound
import net.benwoodworth.knbt.putNbtList

data class InventoryFillingShulker(val numItems: Int) : TriggerItem {
    override fun getNbt(slot: Int): NbtCompound {
        return buildNbtCompound {
            put("Slot", slot.toByte())
            put("Count", 1.toByte())
            put("id", "minecraft:white_shulker_box")
            putNbtCompound("tag") {
                putNbtCompound("BlockEntityTag") {
                    putNbtList("Items") {
                        for (inShulkerSlot in 0 until numItems) {
                            addNbtCompound {
                                put("Slot", inShulkerSlot.toByte())
                                put("Count", 1.toByte())
                                put("id", "minecraft:bedrock")
                                putNbtCompound("tag") {
                                    put("a", inShulkerSlot) // To make each of the bedrock items distinct
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}