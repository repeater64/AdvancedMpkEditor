package me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar

import me.repeater64.advancedmpkeditor.backend.CommandsManager
import me.repeater64.advancedmpkeditor.backend.data_object.AllCommandsSettings
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.FixedSlotsData
import me.repeater64.advancedmpkeditor.backend.data_object.junk.JunkSettings
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.DifficultyOption
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.GamemodeOption
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.ItemBasedOptionEnum
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.PracticeTypeOption
import me.repeater64.advancedmpkeditor.backend.data_object.random_slot.RandomSlotsData
import me.repeater64.advancedmpkeditor.backend.data_object.trigger_items.HotbarFillingChest
import me.repeater64.advancedmpkeditor.backend.data_object.trigger_items.InventoryFillingShulker
import me.repeater64.advancedmpkeditor.backend.data_object.trigger_items.WritableAutoBookItem
import me.repeater64.advancedmpkeditor.backend.data_object.trigger_items.WrittenBookItem
import net.benwoodworth.knbt.NbtCompound
import net.benwoodworth.knbt.NbtList
import net.benwoodworth.knbt.NbtString
import net.benwoodworth.knbt.add
import net.benwoodworth.knbt.buildNbtCompound
import net.benwoodworth.knbt.put
import net.benwoodworth.knbt.putNbtCompound
import net.benwoodworth.knbt.putNbtList

data class BarrelItem(
    val name: String,
    val practiceTypeOption: PracticeTypeOption,
    val gamemodeOption: GamemodeOption,
    val difficultyOption: DifficultyOption,
    val fixedSlotsData: FixedSlotsData,
    val randomSlotsData: RandomSlotsData,
    val junkSettings: JunkSettings
)
    : SavedHotbarItem() {

    override fun getTag(): NbtCompound {
        val (commands, info, numTopLeftInvSlotsToFillLikeHotbar) = CommandsManager.generateCommands(AllCommandsSettings(fixedSlotsData, randomSlotsData, junkSettings))
        return buildNbtCompound {
            put("Count", 1.toByte())
            put("id", "minecraft:barrel")
            putNbtCompound("tag") {
                putNbtCompound("display") {
                    putNbtList("Lore") {
                        add("\"(+NBT)\"")
                    }
                    put("Name", "{\"text\":\"$name\"}")
                }
                putNbtCompound("BlockEntityTag") {
                    put("id", "minecraft:barrel")
                    put("CustomName", "{\"text\":\"$name\"}")
                    putNbtList("Items") {
                        add(HotbarFillingChest(numTopLeftInvSlotsToFillLikeHotbar).getNbt(0))
                        add(InventoryFillingShulker(27-numTopLeftInvSlotsToFillLikeHotbar).getNbt(1))
                        add(WritableAutoBookItem(commands).getNbt(2))
                        add(practiceTypeOption.getItemNBT(3))
                        add(gamemodeOption.getItemNBT(4))
                        add(difficultyOption.getItemNBT(5))
                        add(WrittenBookItem(info).getNbt(26))
                    }
                }
            }
        }
    }

    companion object {
        fun fromTag(tag: NbtCompound) : BarrelItem {
            val blockEntityTag = (tag["tag"] as NbtCompound)["BlockEntityTag"] as NbtCompound

            val customName = (blockEntityTag["CustomName"] as NbtString).value
            val name = customName.substring(9..(customName.length-3))

            val items = blockEntityTag["Items"] as NbtList<*>
            val practiceTypeOption = ItemBasedOptionEnum.getFromNbt(items[3] as NbtCompound, PracticeTypeOption.entries) as PracticeTypeOption
            val gamemodeOption = ItemBasedOptionEnum.getFromNbt(items[4] as NbtCompound, GamemodeOption.entries) as GamemodeOption
            val difficultyOption = ItemBasedOptionEnum.getFromNbt(items[5] as NbtCompound, DifficultyOption.entries) as DifficultyOption

            val serializedPages = ((((items[6] as NbtCompound)["tag"]) as NbtCompound)["pages"] as NbtList<*>).map { it as NbtString }.map { it.value }

            val allCommandsSettings = CommandsManager.loadSettings(serializedPages)

            return BarrelItem(name, practiceTypeOption, gamemodeOption, difficultyOption, allCommandsSettings.fixedSlotsData, allCommandsSettings.randomSlotsData, allCommandsSettings.junkSettings)
        }
    }
}