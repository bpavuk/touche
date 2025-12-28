package dev.bpavuk.touche.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bpavuk.touche.ui.components.BackButton
import dev.bpavuk.touche.ui.theme.ToucheTheme
import org.intellij.lang.annotations.Language

@Composable
private fun StylusScreenArt(modifier: Modifier = Modifier) {
    Box(modifier) {
        // TODO
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StylusScreen(
    onBackPressed: () -> Unit,
    onStylusToggle: (Boolean) -> Unit,
    stylusEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text("Stylus")
                },
                navigationIcon = {
                    BackButton(onBackPressed)
                }
            )
        }
    ) { paddingValues ->
        Row(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Spacer(modifier = Modifier.size(32.dp))
            Column(Modifier.weight(1f)) {
                Spacer(modifier = Modifier.size(32.dp))
                StylusScreenArt(
                    Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 200.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = MaterialTheme.shapes.extraLarge
                        )
                )
                Spacer(modifier = Modifier.size(44.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.medium
                        )
                        .clip(MaterialTheme.shapes.medium)
                        .clickable(onClick = { onStylusToggle(!stylusEnabled) })
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Enable stylus input",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(
                        modifier = Modifier
                            .weight(1f, fill = true)
                    )
                    Switch(
                        checked = stylusEnabled,
                        onCheckedChange = onStylusToggle,
                    )
                }
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = AnnotatedString.fromHtml(
                        @Language("HTML")
                        """
                            <p>
                                touché lets you use your screen as a graphics tablet.
                                <b>You must have a stylus to use this functionality.</b>
                                Usually, it's S Pen or moto stylus. If you don't want that,
                                you can easily turn it off.
                            </p>
                        """.trimIndent()
                    ),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(modifier = Modifier.size(32.dp))
        }
    }
}

@Suppress("AssignedValueIsNeverRead")
@Preview
@Composable
private fun StylusScreenPreview() {
    ToucheTheme(darkTheme = false) {
        var stylusEnabled by remember { mutableStateOf(true) }
        StylusScreen(
            onBackPressed = {},
            onStylusToggle = { stylusEnabled = it },
            stylusEnabled = stylusEnabled,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Suppress("AssignedValueIsNeverRead")
@Preview
@Composable
private fun StylusScreenDarkPreview() {
    ToucheTheme(darkTheme = true) {
        var stylusEnabled by remember { mutableStateOf(true) }
        StylusScreen(
            onBackPressed = {},
            onStylusToggle = { stylusEnabled = !it },
            stylusEnabled = stylusEnabled,
            modifier = Modifier.fillMaxSize()
        )
    }
}
