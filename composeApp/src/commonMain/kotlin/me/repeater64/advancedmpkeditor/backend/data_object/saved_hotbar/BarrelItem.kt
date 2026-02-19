package me.repeater64.advancedmpkeditor.backend.data_object.saved_hotbar

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import me.repeater64.advancedmpkeditor.backend.commands.CommandsManager
import me.repeater64.advancedmpkeditor.backend.data_object.AllCommandsSettings
import me.repeater64.advancedmpkeditor.backend.data_object.fire_res.FireResSettings
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.FixedSlotsData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.HotbarSlotData
import me.repeater64.advancedmpkeditor.backend.data_object.fixed_slot.InventorySlotData
import me.repeater64.advancedmpkeditor.backend.data_object.health_hunger.HealthHungerSettings
import me.repeater64.advancedmpkeditor.backend.data_object.item.DontReplaceMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.EnchantedBootsItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.FireResItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.ForcedEmptyMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.LootingSwordItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.MinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.SimpleMinecraftItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.SoulSpeedBookItem
import me.repeater64.advancedmpkeditor.backend.data_object.item.SplashFireResItem
import me.repeater64.advancedmpkeditor.backend.data_object.junk.JunkSettings
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.DifficultyOption
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.GamemodeOption
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.ItemBasedOptionEnum
import me.repeater64.advancedmpkeditor.backend.data_object.misc_options.PracticeTypeOption
import me.repeater64.advancedmpkeditor.backend.data_object.random_slot.RandomSlotOptionsSet
import me.repeater64.advancedmpkeditor.backend.data_object.random_slot.RandomSlotsData
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.RandomiserCondition
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOption
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionList
import me.repeater64.advancedmpkeditor.backend.data_object.randomiser.WeightedOptionNoLinks
import me.repeater64.advancedmpkeditor.backend.data_object.trigger_items.HotbarFillingChest
import me.repeater64.advancedmpkeditor.backend.data_object.trigger_items.InventoryFillingShulker
import me.repeater64.advancedmpkeditor.backend.data_object.trigger_items.WritableAutoBookItem
import me.repeater64.advancedmpkeditor.backend.data_object.trigger_items.WrittenBookItem
import me.repeater64.advancedmpkeditor.backend.io.NBT
import me.repeater64.advancedmpkeditor.backend.presets_examples.BlankBarrel
import me.repeater64.advancedmpkeditor.backend.presets_examples.condition
import me.repeater64.advancedmpkeditor.backend.presets_examples.optionList
import me.repeater64.advancedmpkeditor.backend.presets_examples.rawItem
import me.repeater64.advancedmpkeditor.util.hash
import net.benwoodworth.knbt.NbtCompound
import net.benwoodworth.knbt.NbtList
import net.benwoodworth.knbt.NbtString
import net.benwoodworth.knbt.add
import net.benwoodworth.knbt.buildNbtCompound
import net.benwoodworth.knbt.put
import net.benwoodworth.knbt.putNbtCompound
import net.benwoodworth.knbt.putNbtList

@Stable
class BarrelItem(
    _name: String,
    _practiceTypeOption: PracticeTypeOption,
    _gamemodeOption: GamemodeOption,
    _difficultyOption: DifficultyOption,
    _fixedSlotsData: FixedSlotsData,
    _randomSlotsData: RandomSlotsData,
    _junkSettings: JunkSettings,
    _healthHungerSettings: HealthHungerSettings,
    _fireResSettings: FireResSettings,
    val allRandomiserLinkLabels: MutableSet<String>
)
    : SavedHotbarItem() {

    var name by mutableStateOf(_name)
    var practiceTypeOption by mutableStateOf(_practiceTypeOption)
    var gamemodeOption by mutableStateOf(_gamemodeOption)
    var difficultyOption by mutableStateOf(_difficultyOption)
    val fixedSlotsData by mutableStateOf(_fixedSlotsData)
    val randomSlotsData by mutableStateOf(_randomSlotsData)
    val junkSettings by mutableStateOf(_junkSettings)
    val healthHungerSettings by mutableStateOf(_healthHungerSettings)
    val fireResSettings by mutableStateOf(_fireResSettings)


    override fun contentHash(): Int {
        return hash(
            name,
            practiceTypeOption,
            gamemodeOption,
            difficultyOption,
            fixedSlotsData.contentHash(),
            randomSlotsData.contentHash(),
            junkSettings.contentHash(),
            healthHungerSettings.contentHash(),
            fireResSettings.contentHash(),
            // Doesn't need to include allRandomiserLinkLabels as if randomiser link labels are changed, that will mean there have been changes elsewhere
        )
    }

    override fun getGuiRepresentationItem(): MinecraftItem {
        return SimpleMinecraftItem("barrel", 1)
    }
    override fun getGuiName() = name

    override fun getTag(): NbtCompound {
        val (commands, info, numTopLeftInvSlotsToFillLikeHotbar) = CommandsManager.generateCommands(AllCommandsSettings(fixedSlotsData, randomSlotsData, junkSettings, healthHungerSettings, fireResSettings, allRandomiserLinkLabels.toMutableList()))
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
        fun fromTag(tag: NbtCompound) : SavedHotbarItem {
            return try {
                fromTagAdvancedMpkEditorGeneratedBarrel(tag)
            } catch (_: Exception) {
                fromTagOtherBarrel(tag)
            }
        }

        private fun fromTagAdvancedMpkEditorGeneratedBarrel(tag: NbtCompound): BarrelItem {
            val blockEntityTag = (tag["tag"] as NbtCompound)["BlockEntityTag"] as NbtCompound

            val customName = (blockEntityTag["CustomName"] as NbtString).value
            val name = customName.substring(9..(customName.length-3))

            val items = blockEntityTag["Items"] as NbtList<*>
            val practiceTypeOption = ItemBasedOptionEnum.getFromNbt(items[3] as NbtCompound, PracticeTypeOption.entries) as PracticeTypeOption
            val gamemodeOption = ItemBasedOptionEnum.getFromNbt(items[4] as NbtCompound, GamemodeOption.entries) as GamemodeOption
            val difficultyOption = ItemBasedOptionEnum.getFromNbt(items[5] as NbtCompound, DifficultyOption.entries) as DifficultyOption

            val serializedPages = ((((items[6] as NbtCompound)["tag"]) as NbtCompound)["pages"] as NbtList<*>).map { it as NbtString }.map { it.value }

            val allCommandsSettings = CommandsManager.loadSettings(serializedPages)

            return BarrelItem(name, practiceTypeOption, gamemodeOption, difficultyOption, allCommandsSettings.fixedSlotsData, allCommandsSettings.randomSlotsData, allCommandsSettings.junkSettings, allCommandsSettings.healthHungerSettings, allCommandsSettings.fireResSettings, allCommandsSettings.allRandomiserLinkLabels.toMutableSet())
        }

        private fun fromTagOtherBarrel(tag: NbtCompound): SavedHotbarItem {
            if (!tag.containsKey("tag")) return AirItem()
            val innerTag = tag["tag"]
            if (innerTag !is NbtCompound) return AirItem()
            if (!innerTag.containsKey("BlockEntityTag")) return AirItem()
            val blockEntityTag = innerTag["BlockEntityTag"]
            if (blockEntityTag !is NbtCompound) return AirItem()

            val name = if (blockEntityTag.containsKey("CustomName")) {
                val customName = (blockEntityTag["CustomName"] as NbtString).value
                if (customName.contains("\"text\"")) {
                    customName.substring(9..(customName.length - 3))
                } else {
                    customName.replace("\"", "")
                }
            } else {
                "Unnamed Barrel"
            }

            val items = blockEntityTag["Items"]
            if (items !is NbtList<*>) return AirItem()


            var practiceTypeOption: PracticeTypeOption? = null
            var gamemodeOption = GamemodeOption.CREATIVE // If no item found, difficulty won't change away from creative
            var difficultyOption = DifficultyOption.NORMAL // Assume world is created in normal, so no difficulty option means probably normal

            var numItemsThatWouldBeInInventory = 0 // Tracks the number of items that the triggers processed so far would lead to the player being given

            val barrel = BlankBarrel.barrel // The barrel data that we are building up. Start with the blank barrel

            val coloredShulkerData: HashMap<String, MutableList<List<MinecraftItem>>> = hashMapOf() // Map of shulker box item ID -> list of content lists of shulker boxes of the same colour
            val whiteShulkerItems = mutableListOf<MinecraftItem>()
            var foundAnyOffhandItems = false

            for (triggerItem in items) {
                ItemBasedOptionEnum.tryGetFromNbt(triggerItem as NbtCompound, PracticeTypeOption.entries)?.let { practiceTypeOption = it as PracticeTypeOption; continue; }
                ItemBasedOptionEnum.tryGetFromNbt(triggerItem, GamemodeOption.entries)?.let { gamemodeOption = it as GamemodeOption; continue; }
                ItemBasedOptionEnum.tryGetFromNbt(triggerItem, DifficultyOption.entries)?.let { difficultyOption = it as DifficultyOption; continue; }

                val triggerItemId = (triggerItem["id"] as NbtString).value
                when (triggerItemId) {
                    "minecraft:chest" -> {
                        val contents = getItemsFromContainerItem(triggerItem)
                        for (item in contents) {
                            val loadedItem = getMinecraftItem(item) ?: continue

                            if (numItemsThatWouldBeInInventory < 9) {
                                barrel.fixedSlotsData.hotbarSlotsData[numItemsThatWouldBeInInventory] = HotbarSlotData(numItemsThatWouldBeInInventory, optionList(WeightedOption(loadedItem)))
                            } else {
                                barrel.fixedSlotsData.inventorySlotsData[numItemsThatWouldBeInInventory-9] = InventorySlotData(numItemsThatWouldBeInInventory-9, optionList(WeightedOption(loadedItem)))
                            }

                            numItemsThatWouldBeInInventory++
                        }
                    }

                    "minecraft:white_shulker_box" -> {
                        val contents = getItemsFromContainerItem(triggerItem)
                        val prevSize = contents.size
                        whiteShulkerItems.addAll(contents.mapNotNull { getMinecraftItem(it) })
                        numItemsThatWouldBeInInventory += (contents.size - prevSize)
                    }
                    "minecraft:brown_shulker_box",
                    "minecraft:red_shulker_box",
                    "minecraft:orange_shulker_box",
                    "minecraft:yellow_shulker_box" -> {
                        val contents = getItemsFromContainerItem(triggerItem)
                        val list = mutableListOf<MinecraftItem>()
                        for (item in contents) {
                            val loadedItem = getMinecraftItem(item) ?: continue
                            list.add(loadedItem)
                        }
                        if (list.isEmpty()) {
                            // This shulker is a "no item" alternative
                            list.add(DontReplaceMinecraftItem())
                        }
                        if (!coloredShulkerData.contains(triggerItemId)) {
                            numItemsThatWouldBeInInventory += list.size // This isn't perfect, we just assume that all shulkers of this colour have the same number of items as the first we found. But that doesn't really matter because this would only be relevant in the unlikely case that someone has a chest AFTER coloured shulkers
                        }
                        coloredShulkerData.getOrPut(triggerItemId) { mutableListOf() }.add(list)
                    }
                    "minecraft:writable_book" -> {
                        if (!triggerItem.containsKey("tag")) continue
                        val innerTag = triggerItem["tag"]
                        if (innerTag !is NbtCompound) continue
                        if (!innerTag.containsKey("display")) continue
                        val displayTag = innerTag["display"]
                        if (displayTag !is NbtCompound) continue
                        if (!displayTag.containsKey("Name")) continue
                        val nameTag = displayTag["Name"]
                        if (nameTag !is NbtString) continue
                        val bookName = nameTag.value

                        if (bookName.contains("AUTO")) {
                            // Auto book - check for common books or commands
                            if (displayTag.contains("Lore")) {
                                val loreTag = displayTag["Lore"]
                                if (loreTag is NbtList<*>) {
                                    if (loreTag.size == 1) {
                                        if (loreTag[0] is NbtString) {
                                            val loreText = (loreTag[0] as NbtString).value

                                            if (loreText.contains("fire resistance and post-bastion")) {
                                                // This is probably the default MPK "give fire resistance and post bastion armor" book
                                                // Give fire res for either 120, 150 or 180s
                                                barrel.fireResSettings.options.options.clear()
                                                barrel.fireResSettings.options.options.add(WeightedOption(120))
                                                barrel.fireResSettings.options.options.add(WeightedOption(150))
                                                barrel.fireResSettings.options.options.add(WeightedOption(180))

                                                // Give EITHER a golden helmet, golden chestplate, or golden leggings. This is a great example of a fairly complicated use of randomiser conditions!
                                                barrel.fixedSlotsData.helmetSlotData.itemOptions.options.clear()
                                                barrel.fixedSlotsData.helmetSlotData.itemOptions.options.add(WeightedOption(rawItem("golden_helmet"), 1, "gold_helmet"))
                                                barrel.fixedSlotsData.helmetSlotData.itemOptions.options.add(WeightedOption(ForcedEmptyMinecraftItem(), 2, "no_helmet"))

                                                barrel.fixedSlotsData.chestplateSlotData.itemOptions.options.clear()
                                                barrel.fixedSlotsData.chestplateSlotData.itemOptions.options.add(WeightedOption(rawItem("golden_chestplate"), 1, _conditions = condition("no_helmet")))
                                                barrel.fixedSlotsData.chestplateSlotData.itemOptions.options.add(WeightedOption(ForcedEmptyMinecraftItem(), 1, "no_chestplate"))

                                                barrel.fixedSlotsData.leggingsSlotData.itemOptions.options.clear()
                                                barrel.fixedSlotsData.leggingsSlotData.itemOptions.options.add(WeightedOption(rawItem("golden_leggings"), 1, _conditions = listOf(RandomiserCondition("no_helmet"), RandomiserCondition("no_chestplate"))))
                                                barrel.fixedSlotsData.leggingsSlotData.itemOptions.options.add(WeightedOption(ForcedEmptyMinecraftItem(), 1, _conditions = listOf(RandomiserCondition("no_helmet", true))))
                                                barrel.fixedSlotsData.leggingsSlotData.itemOptions.options.add(WeightedOption(ForcedEmptyMinecraftItem(), 1, _conditions = listOf(RandomiserCondition("no_chestplate", true))))

                                                // maybe give boots (10% nothing, 20% ss1, 30% ss2, 40% ss3)
                                                barrel.fixedSlotsData.bootsSlotData.itemOptions.options.clear()
                                                barrel.fixedSlotsData.bootsSlotData.itemOptions.options.add(WeightedOption(ForcedEmptyMinecraftItem(), 1))
                                                barrel.fixedSlotsData.bootsSlotData.itemOptions.options.add(WeightedOption(EnchantedBootsItem(true, 1), 2))
                                                barrel.fixedSlotsData.bootsSlotData.itemOptions.options.add(WeightedOption(EnchantedBootsItem(true, 2), 3))
                                                barrel.fixedSlotsData.bootsSlotData.itemOptions.options.add(WeightedOption(EnchantedBootsItem(true, 3), 4))
                                            } else if (loreText.contains("double travel portal")) {
                                                // This is probably the default MPK "simulate double travel portal" book
                                                // TODO
                                            }

                                            // Check actual book contents to see if it sets offhand item
                                            if (innerTag.containsKey("pages")) {
                                                val pagesTag = innerTag["pages"]
                                                if (pagesTag is NbtList<*>) {
                                                    for (pageTag in pagesTag) {
                                                        if (pageTag is NbtString) {
                                                            val cmd = pageTag.value
                                                            if (cmd.contains("replaceitem entity") && cmd.contains("weapon.offhand ")) {
                                                                // We appear to be setting offhand item
                                                                val endBit = cmd.split("weapon.offhand ")[1]
                                                                val split = endBit.split(" ")
                                                                if (split.size != 0) {
                                                                    val id = split[0].removePrefix("minecraft:")
                                                                    val amount = if (split.size > 1) split[1].toIntOrNull() ?: 0 else 0

                                                                    if (!foundAnyOffhandItems) {
                                                                        barrel.fixedSlotsData.offhandSlotData.itemOptions.options.clear()
                                                                    }
                                                                    barrel.fixedSlotsData.offhandSlotData.itemOptions.options.add(WeightedOption(SimpleMinecraftItem(id, amount)))
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (bookName.contains("Surface Blind")) {
                            // TODO
                        } else if (bookName.contains("Force Perch")) {
                            // TODO
                        }
                    }
                }
            }

            if (practiceTypeOption == null) return AirItem()

            // Handle coloured shulker data
            for (itemListList in coloredShulkerData.values) {
                val optionsSets = barrel.randomSlotsData.optionsSets
                optionsSets.add(RandomSlotOptionsSet("Unnamed Item Set", WeightedOptionList(itemListList.map { WeightedOption(it.toMutableStateList()) }.toMutableList())))
            }

            // Handle white shulker data - need to sort into junk and not junk, and for not junk need to group repeated items or generally groupable items
            barrel.junkSettings.junkList.clear()
            val randomItemsetsMap = hashMapOf<String, MutableList<MinecraftItem>>()
            for (item in whiteShulkerItems) {
                if (isJunk(item, practiceTypeOption)) {
                    barrel.junkSettings.junkList.add(WeightedOptionNoLinks(item))
                    continue
                }

                val list = randomItemsetsMap.getOrPut(getItemsetNameForItem(item)) { mutableListOf() }
                var foundMatching = false
                for ((index, existingItem) in list.withIndex()) {
                    if (existingItem is SimpleMinecraftItem && existingItem.equalsIgnoringAmount(item)) {
                        list[index] = existingItem.copyWithAmount(existingItem.amount + item.amount)
                        foundMatching = true
                        break
                    }
                }
                if (!foundMatching) list.add(item)
            }
            for ((setName, items) in randomItemsetsMap) {
                barrel.randomSlotsData.optionsSets.add(RandomSlotOptionsSet(setName, WeightedOptionList(mutableListOf(WeightedOption(items.toMutableStateList())))))
            }

            if (barrel.junkSettings.junkList.isEmpty()) {
                // Ensure we don't have an empty junk list, but disable junk if we didn't find any
                barrel.junkSettings.junkList.add(WeightedOptionNoLinks(DontReplaceMinecraftItem()))
                barrel.junkSettings.enableJunk = false
            }


            barrel.practiceTypeOption = practiceTypeOption
            barrel.gamemodeOption = gamemodeOption
            barrel.difficultyOption = difficultyOption
            barrel.name = name

            return barrel
        }

        private fun getItemsFromContainerItem(containerItem: NbtCompound) : List<NbtCompound> {
            if (!containerItem.containsKey("tag")) return emptyList()
            val innerTag = containerItem["tag"]
            if (innerTag !is NbtCompound) return emptyList()
            if (!innerTag.containsKey("BlockEntityTag")) return emptyList()
            val blockEntityTag = innerTag["BlockEntityTag"]
            if (blockEntityTag !is NbtCompound) return emptyList()

            if (!blockEntityTag.containsKey("Items")) return emptyList()
            val items = blockEntityTag["Items"]
            if (items !is NbtList<*>) return emptyList()

            return items.toList() as List<NbtCompound>
        }

        private fun getMinecraftItem(item: NbtCompound) : MinecraftItem? {
            val id = (item["id"] as NbtString).value
            if (id == "minecraft:air") return null
            return when (id) {
                "minecraft:splash_potion" -> SplashFireResItem()
                "minecraft:potion" -> FireResItem()
                "minecraft:enchanted_book" -> SoulSpeedBookItem(1) // Assume any enchanted book is soul speed. And don't bother to read the level since it really doen't matter
                "minecraft:iron_boots", "minecraft:golden_boots" -> {
                    val simpleItem = if (id == "minecraft:iron_boots") rawItem("iron_boots") else rawItem("golden_boots")

                    val enchantments = getEnchantments(item) ?: return simpleItem
                    for (enchantment in enchantments) {
                        if (enchantment.first.contains("soul_speed")) {
                            val iron = id == "minecraft:iron_boots"
                            return EnchantedBootsItem(iron, enchantment.second)
                        }
                    }
                    simpleItem // If no soul speed enchant found
                }
                "minecraft:golden_sword" -> {
                    val simpleItem = rawItem("golden_sword")

                    val enchantments = getEnchantments(item) ?: return simpleItem
                    for (enchantment in enchantments) {
                        if (enchantment.first.contains("looting")) {
                            return LootingSwordItem(enchantment.second)
                        }
                    }
                    simpleItem // If no looting enchant found
                }
                else -> {
                    val count = NBT.getCount(item)
                    SimpleMinecraftItem(id.removePrefix("minecraft:"), count)
                }
            }
        }

        private fun getEnchantments(item: NbtCompound): List<Pair<String, Int>>? {
            if (!item.containsKey("tag")) return null
            val innerTag = item["tag"]
            if (innerTag !is NbtCompound) return null
            if (!innerTag.containsKey("Enchantments")) return null
            val enchantmentsTag = innerTag["Enchantments"]
            if (enchantmentsTag !is NbtList<*>) return null

            val list = mutableListOf<Pair<String, Int>>()

            for (enchantmentTag in enchantmentsTag) {
                if (enchantmentTag !is NbtCompound) return null
                if (!enchantmentTag.containsKey("id")) return null
                val id = enchantmentTag["id"]
                if (id !is NbtString) return null

                val lvl = NBT.getNumber(enchantmentTag, "lvl", 1)
                list.add(Pair(id.value, lvl))
            }
            return list
        }

        private fun isJunk(item: MinecraftItem, practiceType: PracticeTypeOption): Boolean {
            return when (practiceType) {
                PracticeTypeOption.BURIED_TREASURE,
                PracticeTypeOption.OCEAN_MONUMENT,
                PracticeTypeOption.SHIPWRECK,
                PracticeTypeOption.NETHER_ENTER,
                PracticeTypeOption.BASTION -> false // Inventory shouldn't be filled with junk by these splits, but probably will by later ones

                else -> {
                    when (item) {
                        is SoulSpeedBookItem -> true
                        is EnchantedBootsItem -> true
                        is LootingSwordItem -> false
                        is FireResItem, is SplashFireResItem -> practiceType == PracticeTypeOption.END_ENTER || practiceType == PracticeTypeOption.STRONGHOLD
                        is SimpleMinecraftItem -> {
                            if (item.id.endsWith("_sapling")) return true
                            if (item.id.endsWith("_door")) return true // Junk by these later splits
                            if (item.id.contains("blackstone")) return true
                            if (item.id.contains("stone_bricks")) return true
                            when (item.id) {
                                "leather" -> true
                                "magma_cream" -> true
                                "quartz" -> true
                                "iron_nugget" -> practiceType.isBlindOrLater
                                "golden_nugget" -> true
                                "nether_brick" -> item.amount <= 10
                                "string" -> item.amount <= 2
                                "glowstone_dust" -> item.amount <= 3
                                "sand" -> true
                                "tnt" -> practiceType.isBlindOrLater
                                "nether_brick_fence" -> true
                                "bone" -> true
                                "iron_ingot", "gold_ingot", "diamond" -> practiceType == PracticeTypeOption.END_ENTER || practiceType == PracticeTypeOption.STRONGHOLD
                                "emerald" -> true
                                "basalt" -> true
                                else -> false
                            }
                        }
                        else -> false
                    }
                }
            }
        }

        private fun getItemsetNameForItem(item: MinecraftItem) : String {
            return when (item) {
                is FireResItem -> "Fire Resistance"
                is SplashFireResItem -> "Fire Resistance"
                is SimpleMinecraftItem -> {
                    if (item.id.endsWith("_bed")) return "Explosives"
                    if (item.id.endsWith("_leaves")) return "Blocks"
                    if (item.id.contains("sandstone")) return "Blocks"
                    if (item.id.endsWith("_log")) return "Crafting"
                    if (item.id.endsWith("_planks")) return "Crafting"
                    if (item.id.endsWith("_boat")) return "Boat"
                    if (item.id.endsWith("_door")) return "Doors"
                    if (item.id.endsWith("bucket")) return "Bucket"
                    if (item.id.endsWith("_pickaxe")) return "Tools"
                    if (item.id.endsWith("_axe")) return "Tools"
                    if (item.id.endsWith("_shovel")) return "Tools"
                    if (item.id.endsWith("_sword")) return "Tools"
                    if (item.id.endsWith("_helmet")) return "Armor"
                    if (item.id.endsWith("_chestplate")) return "Armor"
                    if (item.id.endsWith("_leggings")) return "Armor"
                    if (item.id.endsWith("_boots")) return "Armor"
                    if (item.id.endsWith("_sapling")) return "Overworld Junk"
                    when (item.id) {
                        "respawn_anchor" -> "Explosives"
                        "glowstone" -> "Explosives"
                        "string" -> "Useful Barters"
                        "glowstone_dust" -> "Useful Barters"
                        "fire_charge" -> "Useful Barters"
                        "ender_pearl" -> "Useful Barters"
                        "obsidian" -> "Useful Barters"
                        "crying_obsidian" -> "Useful Barters"
                        "nether_brick" -> "Useful Barters"
                        "iron_nugget" -> "Resources"
                        "iron_ingot" -> "Resources"
                        "gold_ingot" -> "Resources"
                        "golden_nugget" -> "Resources"
                        "diamond" -> "Resources"
                        "cobblestone" -> "Resources"
                        "crafting_table" -> "Crafting"
                        "stick" -> "Crafting"
                        "soul_sand" -> "Blocks"
                        "gravel" -> "Blocks"
                        "dirt" -> "Blocks"
                        "netherrack" -> "Blocks"
                        "nether_bricks" -> "Blocks"
                        "coarse_dirt" -> "Blocks"
                        "bow" -> "Projectiles"
                        "crossbow" -> "Projectiles"
                        "arrow" -> "Projectiles"
                        "spectral_arrow" -> "Projectiles"
                        "ender_eye" -> "Eyes + Rods"
                        "blaze_rod" -> "Eyes + Rods"
                        "blaze_powder" -> "Eyes + Rods"
                        "bread" -> "Food"
                        "apple" -> "Food"
                        "carrot" -> "Food"
                        "cooked_porkchop" -> "Food"
                        "cooked_beef" -> "Food"
                        "cooked_cod" -> "Food"
                        "cooked_salmon" -> "Food"
                        "cooked_mutton" -> "Food"
                        "cooked_chicken" -> "Food"
                        "golden_carrot" -> "Food"
                        "golden_apple" -> "Food"
                        "enchanted_golden_apple" -> "Food"
                        "suspicious_stew" -> "Food"
                        "mushroom_stew" -> "Food"
                        "rotten_flesh" -> "Food"
                        "sand" -> "Overworld Junk"
                        "light_weighted_pressure_plate" -> "Distraction Gold"
                        "golden_horse_armor" -> "Distraction Gold"
                        "clock" -> "Distraction Gold"
                        "glistering_melon_slice" -> "Distraction Gold"
                        else -> item.displayName
                    }
                }
                else -> item.displayName
            }
        }
    }
}