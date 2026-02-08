package me.repeater64.advancedmpkeditor.backend.data_object.trigger_items

import net.benwoodworth.knbt.NbtCompound

interface TriggerItem {
    fun getNbt(slot: Int): NbtCompound
}