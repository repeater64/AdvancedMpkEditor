package me.repeater64.advancedmpkeditor.backend.data_object.item

class FireResItem : MinecraftItem {
    override val commandString = "potion{CustomPotionColor:16351261,CustomPotionEffects:[{Id:12,Duration:3600}],display:{Name:'[{\"text\":\"Potion of Fire Resistance\",\"italic\":false}]'}} 1"
    override val displayName = "Potion of Fire Resistance"
    override val amount = 1
    override val iconFile = "fire_res.png"

    companion object : MinecraftItemFactory<FireResItem> {
        override val pattern = "\"Potion of"
        override fun create(command: String) = FireResItem()
    }
}