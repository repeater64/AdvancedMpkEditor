package me.repeater64.advancedmpkeditor.backend.data_object.item

class ForcedEmptyMinecraftItem : MinecraftItem {
    override val amount: Int = 1
    override val commandString: String
        get() {
            return "minecraft:barrier{\"id\":${identifier++}}"
        }
    override val displayName = "Forced Empty Slot"
    override val iconFile = "barrier.png"

    companion object : MinecraftItemFactory<ForcedEmptyMinecraftItem> {
        var identifier: Int = 100

        override val pattern = "barrier{"
        override fun create(command: String) = ForcedEmptyMinecraftItem()
    }
}