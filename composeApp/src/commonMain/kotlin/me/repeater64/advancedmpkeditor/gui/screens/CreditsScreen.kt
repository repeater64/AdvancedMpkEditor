package me.repeater64.advancedmpkeditor.gui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import me.repeater64.advancedmpkeditor.gui.platform.openUrl

@Composable
fun CreditsScreen() {
    val linkStyle = SpanStyle(
        color = Color(0xFF0000EE),
        textDecoration = TextDecoration.Underline
    )

    val textLinkStyles = TextLinkStyles(style = linkStyle)

    fun createLink(url: String): LinkAnnotation.Url {
        return LinkAnnotation.Url(
            url = url,
            styles = textLinkStyles,
            linkInteractionListener = { openUrl(url) }
        )
    }

    val creditsText = buildAnnotatedString {
        append("This tool was created by repeater64 - feel free to DM on discord if you have any issues or questions.\n\n")

        append("Thanks to ")

        withLink(createLink("https://github.com/Knawk/mc-MiniPracticeKit")) {
            append("Knawk")
        }

        append(" for creating MiniPracticeKit itself (this tool is not affiliated with MiniPracticeKit).\n")

        append("Thanks to ")

        withLink(createLink("https://github.com/qMaxXen/MiniPracticeKit-Editor")) {
            append("qMaxXen")
        }

        append(" who created a simpler MPK editor website, for generally inspiring some aspects of the GUI layout.\n")

        append("Thanks to ")

        withLink(createLink("https://github.com/bezzdev/search_crafting/tree/master/public/icons")) {
            append("Bezzdev")
        }

        append(" for all the item icons.")
    }

    BasicFullyScrollableScreen(Modifier.fillMaxWidth()) {
        Text(
            text = "Credits",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = creditsText,
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = MaterialTheme.typography.bodyLarge.fontSize * 1.5,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}