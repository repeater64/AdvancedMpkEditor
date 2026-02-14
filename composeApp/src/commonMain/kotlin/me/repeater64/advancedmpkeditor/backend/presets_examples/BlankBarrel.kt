package me.repeater64.advancedmpkeditor.backend.presets_examples

import me.repeater64.advancedmpkeditor.backend.data_object.fire_res.FireResSettings
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.BootsSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.ChestplateSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.FixedSlotsData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.HelmetSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.LeggingsSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.OffhandSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HealthHungerOption
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HealthHungerSettings
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HealthOption
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HungerOption
import me.repeater64.advancedmpkeditor.backend.data_object.item.EnchantedBootsItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.FireResItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.SplashFireResItem
import me.repeater64.advancedmpkeditor.backend.data_object.junk.JunkSettings
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.DifficultyOption
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.GamemodeOption
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.PracticeTypeOption
import me.repeater64.advancedmpkeditor.backend.data_object.random_slot.RandomSlotOptionsSet
import me.repeater64.advancedmpkeditor.backend.data_object.random_slot.RandomSlotsData
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar.BarrelItem

object BlankBarrel {
    private val fixedSlotsData get() = FixedSlotsData(
        OffhandSlotData(optionList(emptyItem())),
        listOf(
            hotbarSlot(0, optionList(emptyItem())),
            hotbarSlot(1, optionList(emptyItem())),
            hotbarSlot(2, optionList(emptyItem())),
            hotbarSlot(3, optionList(emptyItem())),
            hotbarSlot(4, optionList(emptyItem())),
            hotbarSlot(5, optionList(emptyItem())),
            hotbarSlot(6, optionList(emptyItem())),
            hotbarSlot(7, optionList(emptyItem())),
            hotbarSlot(8, optionList(emptyItem())),
        ),
        listOf(
            invSlot(0, optionList(availableItem())),
            invSlot(1, optionList(availableItem())),
            invSlot(2, optionList(availableItem())),
            invSlot(3, optionList(availableItem())),
            invSlot(4, optionList(availableItem())),
            invSlot(5, optionList(availableItem())),
            invSlot(6, optionList(availableItem())),
            invSlot(7, optionList(availableItem())),
            invSlot(8, optionList(availableItem())),
            invSlot(9, optionList(availableItem())),
            invSlot(10, optionList(availableItem())),
            invSlot(11, optionList(availableItem())),
            invSlot(12, optionList(availableItem())),
            invSlot(13, optionList(availableItem())),
            invSlot(14, optionList(availableItem())),
            invSlot(15, optionList(availableItem())),
            invSlot(16, optionList(availableItem())),
            invSlot(17, optionList(availableItem())),
            invSlot(18, optionList(availableItem())),
            invSlot(19, optionList(availableItem())),
            invSlot(20, optionList(availableItem())),
            invSlot(21, optionList(availableItem())),
            invSlot(22, optionList(availableItem())),
            invSlot(23, optionList(availableItem())),
            invSlot(24, optionList(availableItem())),
            invSlot(25, optionList(availableItem())),
            invSlot(26, optionList(availableItem()))
        ),
        HelmetSlotData(optionList(emptyItem())),
        ChestplateSlotData(optionList(emptyItem())),
        LeggingsSlotData(optionList(emptyItem())),
        BootsSlotData(optionList(emptyItem())),
    )

    private val randomSlotsData get() = RandomSlotsData(emptyList())

    private val junkSettings get() = JunkSettings(true, true, listOf(
        rawItem("leather", 64),
    ))

    private val healthHungerSettings get() = HealthHungerSettings(WeightedOptionList(mutableListOf(
        WeightedOption(HealthHungerOption(HealthOption.FULL_HEALTH, HungerOption.HUNGER_RESET), 1),
    )))

    private val fireResSettings get() = FireResSettings(WeightedOptionList(mutableListOf(
        WeightedOption(0, 1)
    )))

    val barrel get() = BarrelItem("Unnammed Barrel", PracticeTypeOption.END_ENTER, GamemodeOption.SURVIVAL, DifficultyOption.EASY, fixedSlotsData, randomSlotsData, junkSettings, healthHungerSettings, fireResSettings, hashSetOf())
}