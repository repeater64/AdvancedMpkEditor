package me.repeater64.advancedmpkeditor.backend.data_object

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.fire_res.FireResSettings
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.FixedSlotsData
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HealthHungerSettings
import me.repeater64.advancedmpkeditor.backend.data_object.junk.JunkSettings
import me.repeater64.advancedmpkeditor.backend.data_object.random_slot.RandomSlotsData

data class AllCommandsSettings(
    val fixedSlotsData: FixedSlotsData,
    val randomSlotsData: RandomSlotsData,
    val junkSettings: JunkSettings,
    val healthHungerSettings: HealthHungerSettings,
    val fireResSettings: FireResSettings,
) {
    companion object : BookSerializable<AllCommandsSettings> {
        override val className = "AllCommandsSettings"

        override fun serializeToPages(it: AllCommandsSettings): List<String> {
            return FixedSlotsData.serialize(it.fixedSlotsData) + RandomSlotsData.serialize(it.randomSlotsData) + JunkSettings.serialize(it.junkSettings) + HealthHungerSettings.serialize(it.healthHungerSettings) + FireResSettings.serialize(it.fireResSettings)
        }

        override fun deserializeFromPages(pages: List<String>): AllCommandsSettings {
            val (fixedSlotsData, pages1) = BookSerializable.getObjectAndRemainingPages(pages, FixedSlotsData)
            val (randomSlotsData, pages2) = BookSerializable.getObjectAndRemainingPages(pages1, RandomSlotsData)
            val (junkSettings, pages3) = BookSerializable.getObjectAndRemainingPages(pages2, JunkSettings)
            val (healthHungerSettings, pages4) = BookSerializable.getObjectAndRemainingPages(pages3, HealthHungerSettings)
            val fireResSettings = BookSerializable.getObjectAndRemainingPages(pages4, FireResSettings).first
            return AllCommandsSettings(fixedSlotsData, randomSlotsData, junkSettings, healthHungerSettings, fireResSettings)
        }

    }
}