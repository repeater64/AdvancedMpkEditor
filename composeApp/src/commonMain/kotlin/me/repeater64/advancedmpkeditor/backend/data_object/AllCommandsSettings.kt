package me.repeater64.advancedmpkeditor.backend.data_object

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializableString
import me.repeater64.advancedmpkeditor.backend.data_object.fire_res.FireResSettings
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.FixedSlotsData
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HealthHungerSettings
import me.repeater64.advancedmpkeditor.backend.data_object.junk.JunkSettings
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.PracticeTypeOption
import me.repeater64.advancedmpkeditor.backend.data_object.random_slot.RandomSlotsData

data class AllCommandsSettings(
    val practiceTypeOption: PracticeTypeOption,
    val fixedSlotsData: FixedSlotsData,
    val randomSlotsData: RandomSlotsData,
    val junkSettings: JunkSettings,
    val healthHungerSettings: HealthHungerSettings,
    val fireResSettings: FireResSettings,
    val allRandomiserLinkLabels: List<String>
) {
    companion object : BookSerializable<AllCommandsSettings> {
        override val className = "AllCommandsSettings"

        override fun serializeToPages(it: AllCommandsSettings): List<String> {
            return listOf(it.practiceTypeOption.name) + FixedSlotsData.serialize(it.fixedSlotsData) + RandomSlotsData.serialize(it.randomSlotsData) + JunkSettings.serialize(it.junkSettings) + HealthHungerSettings.serialize(it.healthHungerSettings) + FireResSettings.serialize(it.fireResSettings) + BookSerializable.serializeList(it.allRandomiserLinkLabels.map { BookSerializableString(it) }, BookSerializableString)
        }

        override fun deserializeFromPages(pages: List<String>): AllCommandsSettings {
            val practiceTypeOption = PracticeTypeOption.valueOf(pages[0])
            val (fixedSlotsData, pages1) = BookSerializable.getObjectAndRemainingPages(pages.drop(1), FixedSlotsData)
            val (randomSlotsData, pages2) = BookSerializable.getObjectAndRemainingPages(pages1, RandomSlotsData)
            val (junkSettings, pages3) = BookSerializable.getObjectAndRemainingPages(pages2, JunkSettings)
            val (healthHungerSettings, pages4) = BookSerializable.getObjectAndRemainingPages(pages3, HealthHungerSettings)
            val (fireResSettings, pages5) = BookSerializable.getObjectAndRemainingPages(pages4, FireResSettings)
            val allRandomiserLinkLabels = BookSerializable.getListAndRemainingPages(pages5, BookSerializableString).first.map { it.string }
            return AllCommandsSettings(practiceTypeOption, fixedSlotsData, randomSlotsData, junkSettings, healthHungerSettings, fireResSettings, allRandomiserLinkLabels)
        }

    }
}