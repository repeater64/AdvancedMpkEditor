package me.repeater64.advancedmpkeditor.backend.data_object.item

class SplashFireResItem : MinecraftItem {
    override val commandString = "splash_potion{CustomPotionColor:16351261,CustomPotionEffects:[{Id:12,Duration:3600}],display:{Name:'[{\"text\":\"Splash Potion of Fire Resistance\",\"italic\":false}]'}} 1"
    override val displayName = "Potion of Fire Resistance"
    override val amount = 1
    override val iconFile = "fire_res.png"

    companion object : MinecraftItemFactory<SplashFireResItem> {
        override val pattern = "splash_potion"
        override fun create(command: String) = SplashFireResItem()
    }
}