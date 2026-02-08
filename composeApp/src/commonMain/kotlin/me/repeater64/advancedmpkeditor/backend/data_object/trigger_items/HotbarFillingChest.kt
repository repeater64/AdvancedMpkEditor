package me.repeater64.advancedmpkeditor.backend.data_object.trigger_items

import net.benwoodworth.knbt.NbtCompound
import net.benwoodworth.knbt.addNbtCompound
import net.benwoodworth.knbt.buildNbtCompound
import net.benwoodworth.knbt.put
import net.benwoodworth.knbt.putNbtCompound
import net.benwoodworth.knbt.putNbtList

object HotbarFillingChest : TriggerItem {
    override fun getNbt(slot: Int): NbtCompound {
        return buildNbtCompound {
            put("Slot", slot.toByte())
            put("Count", 1.toByte())
            put("id", "minecraft:chest")
            putNbtCompound("tag") {
                putNbtCompound("BlockEntityTag") {
                    put("id", "minecraft:chest")
                    putNbtList("Items") {
                        for (inChestSlot in 0..8) {
                            addNbtCompound {
                                put("Slot", inChestSlot.toByte())
                                put("Count", 1.toByte())
                                put("id", "minecraft:barrier")
                                putNbtCompound("tag") {
                                    put("a", inChestSlot.toByte()) // To make each of the barrier items distinct
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}