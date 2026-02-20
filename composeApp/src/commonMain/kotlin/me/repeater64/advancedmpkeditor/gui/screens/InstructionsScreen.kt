package me.repeater64.advancedmpkeditor.gui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InstructionsScreen() {

    BasicFullyScrollableScreen(Modifier.fillMaxWidth()) {
        Column(Modifier.width(1200.dp)) {
            Text(
                text = "Instructions",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "The Basic Idea",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(15.dp))

            Text(
                text = """
                From the home screen, you can either start editing the example template (recommended), or upload your previous hotbar.nbt file if you've used MPK before. 
                In the main editor, you can set up a hotbar arrangement of barrels, and also the MPK command block. Each barrel corresponds to the settings for practicing a certain split. 
                Click on a barrel to edit the settings. You can change which split you want to practice, set up your inventory, random items, etc.
                When you're done, press "Download". This will download a hotbar.nbt file. Place this into your .minecraft folder for the instance you want to practice on. If you already have a hotbar.nbt file in there, you may want to make a backup before overwriting.
                Open or re-launch your Minecraft after putting in the hotbar.nbt file. 
                To use MPK, create a new world with gamemode Creative. Then press x+1 to load your first saved hotbar (check your keybinds if you've changed it to something other than x). This will give you the hotbar with barrels and the command block.
                Drop the barrel on the ground for the split you want to practice. Then place down the command block. Then you will be given all your items and teleported into that split (some splits require following instructions in chat).
                To keep editing your setup if you've closed the tab, you can upload your hotbar.nbt file that you previously downloaded. 
            """.trimIndent(),
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = MaterialTheme.typography.bodyLarge.fontSize * 1.5
            )

            Spacer(Modifier.height(15.dp))

            Text(
                text = "Barrel Settings",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(15.dp))

            Text(
                text = """
                You can chance the following settings for a given barrel:
                  - The practice type: Choose which split you want to practice
                  - Gamemode + Difficulty: Recommended to keep as "Survival" and "Easy"
                  - Fixed Slot Items: There is an inventory view where you can set up all items that should go in consistent slots (ie your hotbar, offhand and armour, and optionally parts of your inventory too). Click a slot to change the items that can go in it.
                  - Random Slot Items: Here you set up items that should go into random spots in your inventory. ONE set of items will be chosen from each of the sections here. Take a look at the example templates to get an idea of how this works.
                  - Junk Settings: You can turn on/off the option to fill empty spots in your inventory with junk items. You can customise the list of items that junk is picked from.
                  - Health + Hunger Settings: You can customise your health and your hunger+saturation that you'll have upon starting.
                  - Fire Res Settings: You can set up the duration of fire resistance effect that you'll have, or zero for no fire resistance.
            """.trimIndent(),
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = MaterialTheme.typography.bodyLarge.fontSize * 1.5
            )

            Spacer(Modifier.height(15.dp))

            Text(
                text = "Weighted Option Lists",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(15.dp))

            Text(
                text = """
                    Almost every setting is based around a weighted list of options - ie multiple options that can be chosen, and you can control how likely they are with respect to each other.
                    The "Weight" column lets you control how likely each option is. Options with equal weight numbers are equally likely, and an option with a weight twice as big as another is twice as likely.
                    You can set up "Randomiser Links" to link the option randomly chosen in one list to another list. This is explained fully below.
                """.trimIndent(),
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = MaterialTheme.typography.bodyLarge.fontSize * 1.5
            )

            Spacer(Modifier.height(15.dp))

            Text(
                text = "Randomiser Links",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(15.dp))

            Text(
                text = """
                    An option in a list can either "trigger" a randomiser link, or depend on one.
                     
                    If you set an option to be a trigger with a certain condition label, then that label will be "triggered" if that option gets chosen, and not triggered if another option gets chosen.
                    
                    If you set an option to be "only if" for a certain condition label, then that option will only be added into the actual pool of valid options if that label gets triggered elsewhere.
                    If you set an option to be "unless" a certain condition label, then that option will be removed from the pool of valid options if that label gets triggered elsewhere.
                    
                    If you add multiple conditions to an option, they are treated as "AND" - ie they must all be true, so if they are "only if"s then ALL of those labels must get triggered for the option to be valid, and if they are "unless"es then NONE of those labels can be triggered for the option to be valid.
                    
                    Once it is known which labels have or haven't been triggered, option lists are filtered down to remove any invalid options because of the specified conditions. Then a random choice is made based on the remaining valid options and their weights.
                    
                    This is best understood with examples. A very simple case is that you want to have a random number of respawn anchors in one hotbar slot, and an equal number of glowstone in another slot.
                    For your anchor slot, you would add all the different numbers of anchors as different options, and set each one as a trigger, triggering conditions labelled "1anchor", "2anchor", etc.
                    Then in your glowstone slot you can add the different amounts of glowstone, and set each one to be "Only If" and then the corresponding condition.
                    
                    For a slightly more complicated version of this same example, see the "End Enter - Hotbar Sorted" example barrel, and how it handles respawn anchors + glowstone.
                    
                    For another good example of a way to use randomiser links, look at the "Fortress" example barrel, and see how the food in the offhand is linked to the blaze bed/tnt situation (and also the health+hunger settings).
                    
                    If you're not sure how to set up what you want, feel free to drop me a message on discord, my username is "repeater64".
                """.trimIndent(),
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = MaterialTheme.typography.bodyLarge.fontSize * 1.5
            )
        }
    }
}