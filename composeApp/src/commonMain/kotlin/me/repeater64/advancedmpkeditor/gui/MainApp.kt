package me.repeater64.advancedmpkeditor.gui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.repeater64.advancedmpkeditor.gui.screens.EditorScreen
import me.repeater64.advancedmpkeditor.gui.screens.HomeScreen
import me.repeater64.advancedmpkeditor.gui.screens.ImportScreen
import me.repeater64.advancedmpkeditor.gui.screens.InstructionsScreen
import me.repeater64.advancedmpkeditor.gui.screens.Screen
import me.repeater64.advancedmpkeditor.gui.platform.HotbarNbtFileManager
import me.repeater64.advancedmpkeditor.gui.platform.openUrl
import me.repeater64.advancedmpkeditor.gui.screens.CreditsScreen

@Composable
fun MainApp(fileManager: HotbarNbtFileManager) {
    val darkColors = darkColorScheme(
        primary = Color(0xFFBB86FC),
        secondary = Color(0xFFCCCCCC),
        tertiary = Color(0xFF444444),
        background = Color(0xFF121212),
        surface = Color(0xFF1E1E1E),
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onBackground = Color.White,
        onSurface = Color.White,
    )

    val typography = Typography(

    )

    MaterialTheme(darkColors, typography = typography) {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

        var pendingNavigationRequest by remember { mutableStateOf<Screen?>(null) }

        fun requestNavigation(destination: Screen) {
            if (currentScreen is Screen.Editor) {
                // If in Editor, send a request
                pendingNavigationRequest = destination
            } else {
                // Otherwise, just go there immediately
                currentScreen = destination
            }
        }


        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Column(modifier = Modifier.fillMaxSize()) {


                Header(
                    onGoHome = { requestNavigation(Screen.Home) },
                    onOpenInstructions = { requestNavigation(Screen.Instructions) },
                    onOpenCredits = { requestNavigation(Screen.Credits) }
                )

                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    when (val screen = currentScreen) {
                        is Screen.Home -> HomeScreen(
                            onOpenEditor = { hotbars -> currentScreen = Screen.Editor(hotbars) },
                            onOpenImport = { currentScreen = Screen.Import }
                        )

                        is Screen.Import -> ImportScreen(
                            fileManager = fileManager,
                            onHotbarsLoaded = { hotbars -> currentScreen = Screen.Editor(hotbars) }
                        )

                        is Screen.Editor -> EditorScreen(
                            hotbars = screen.hotbars,
                            fileManager = fileManager,

                            pendingNavigationRequest = pendingNavigationRequest,
                            onConfirmNavigation = { destination ->
                                currentScreen = destination
                                pendingNavigationRequest = null // Reset request
                            },
                            onCancelNavigation = {
                                pendingNavigationRequest = null // Reset request
                            }
                        )

                        is Screen.Instructions -> InstructionsScreen()
                        is Screen.Credits -> CreditsScreen()
                    }

                }


                Footer()
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    onGoHome: () -> Unit,
    onOpenInstructions: () -> Unit,
    onOpenCredits: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Advanced MPK Editor",
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onGoHome()
                }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(),
        actions = {
            IconButton(onClick = onGoHome) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Go Home",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            TextButton(onClick = onOpenInstructions) {
                Text(
                    "Instructions",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            TextButton(onClick = onOpenCredits) {
                Text(
                    "Credits",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            TextButton(onClick = { openUrl("https://github.com/repeater64/AdvancedMpkEditor") }) {
                Text(
                    "Github",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}

@Composable
fun Footer() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Advanced MPK Editor - repeater64 - v1.0",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
    }
}