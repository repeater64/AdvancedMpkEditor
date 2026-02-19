package me.repeater64.advancedmpkeditor.backend.data_object.item

import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializable
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.BookSerializableNoAttributes
import me.repeater64.advancedmpkeditor.backend.data_object.book_serialization.NoAttributesDataClass

class SurfaceBlindPotionItem : NoAttributesDataClass(), MinecraftItem {
    override val commandEndBit = "lingering_potion{pages:[\"execute in overworld unless entity @p[x=0] run say Must be in overworld to re-blind on surface!\",\"execute in overworld unless entity @p[x=0] run data remove storage pk I[0][]\",\"say Re-blinding on surface!\",\"execute at @p run fill ~-16 ~-16 ~-16 ~15 ~15 ~15 air replace nether_portal\",\"execute at @p run summon armor_stand ~ ~ ~ {Marker:1,Tags:[p]}\",\"execute at @p run summon armor_stand ~ ~ ~ {Marker:1,Tags:[p]}\",\"execute at @e[tag=p] run summon armor_stand ~ ~ ~ {Marker:1,Tags:[p]}\",\"execute at @e[tag=p] run summon armor_stand ~ ~ ~ {Marker:1,Tags:[p]}\",\"execute at @e[tag=p] run summon armor_stand ~ ~ ~ {Marker:1,Tags:[p]}\",\"data merge storage pk {H:1}\",\"execute at @p store success score !s pk run spreadplayers ~ ~ 6 24 false @e[tag=p]\",\"data merge storage pk {H:1}\",\"execute if score !s pk matches 0 at @p run fill ~ 72 ~ ~2 72 ~1 obsidian\",\"execute if score !s pk matches 0 at @p run tp @e[tag=p] ~1 73 ~\",\"execute as @e[tag=p] at @s store result score @s pk if blocks ~-3 ~ ~-3 ~3 ~4 ~3 ~-3 ~ ~-3 masked\",\"scoreboard players set !m pk 999\",\"execute as @e[tag=p] run scoreboard players operation !m pk < @s pk\",\"execute as @e[tag=p] unless score !m pk = @s pk run kill @s\",\"tag @e[tag=p,sort=random,limit=1] add pp\",\"kill @e[tag=p,tag=!pp]\",\"execute as @e[tag=p] at @s unless block ^ ^ ^1 air run tp @s ~ ~ ~ ~90 0\",\"execute as @e[tag=p] at @s unless block ^ ^ ^1 air run tp @s ~ ~ ~ ~90 0\",\"execute as @e[tag=p] at @s unless block ^ ^ ^1 air run tp @s ~ ~ ~ ~90 0\",\"execute at @e[tag=p] run fill ^ ^-.5 ^-1 ^ ^3.5 ^2 obsidian\",\"execute at @e[tag=p] run fill ^ ^.5 ^ ^ ^2.5 ^1 air\",\"execute at @e[tag=p] run setblock ^ ^ ^ fire\",\"data merge storage pk {H:1}\",\"execute at @e[tag=p] run tp @p ~ ~ ~ ~90 ~\",\"kill @e[tag=p]\"],display:{Name:'{\"text\":\"Surface Blind\"}',Lore:['{\"text\":\"Re-blind on surface\"}']},CustomPotionColor:11612159,HideFlags:255} 1"
    override val displayName = "\"Re-blind on Surface\" MPK Potion"
    override val amount = 1
    override val iconFile = "surface_blind_potion.png"
    override val numStacks = 1
    override val stackSize = 1
    override val companion = Companion as BookSerializable<MinecraftItem>

    companion object : BookSerializableNoAttributes<SurfaceBlindPotionItem> {
        override fun createObject() = SurfaceBlindPotionItem()
        override val className = "SurfaceBlindPotionItem"
    }
}