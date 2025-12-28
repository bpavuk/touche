
package dev.bpavuk.touche.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bpavuk.touche.ui.components.BackButton
import dev.bpavuk.touche.ui.surfaces.Cloudy
import dev.bpavuk.touche.ui.theme.ToucheTheme

data class Screensaver(
    val id: String,
    val displayName: String,
    val animation: @Composable (Modifier) -> Unit,
)

object Screensavers {
    val cloudy = Screensaver(
        "cloudy",
        "Cloudy",
        ::Cloudy
    )

    fun all(): List<Screensaver> {
        // TODO: replace with a single "cloudy" instance
        return listOf(
            cloudy,
            cloudy,
            cloudy,
            cloudy
        )
    }
}

@Composable
fun ScreensaverScreenArt(modifier: Modifier = Modifier) {
    Box(modifier) {
        // TODO
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreensaverSettingsScreen(
    onBackPressed: () -> Unit,
    onScreensaverToggle: (Boolean) -> Unit,
    onScreensaverChange: (Screensaver) -> Unit,
    screensaverEnabled: Boolean,
    screensaverAnimations: List<Screensaver>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text("Screensaver")
                },
                navigationIcon = {
                    BackButton(onBackPressed)
                }
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(136.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 32.dp),
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    Spacer(modifier = Modifier.size(32.dp))
                    ScreensaverScreenArt(
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
                            .clickable(onClick = { onScreensaverToggle(!screensaverEnabled) })
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Enable screensaver",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(
                            modifier = Modifier
                                .weight(1f, fill = true)
                        )
                        Switch(
                            checked = screensaverEnabled,
                            onCheckedChange = onScreensaverToggle,
                        )
                    }
                    Spacer(Modifier.size(8.dp))
                    Text(
                        text = "Display the full-screen animation when touché is active",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(Modifier.size(40.dp))
                    Text(
                        text = "Animations",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(Modifier.size(16.dp))
                }
            }

            items(
                items = screensaverAnimations,
            ) { screensaver ->
                Card(
                    onClick = { onScreensaverChange(screensaver) },
                    modifier = Modifier.defaultMinSize(minHeight = 200.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp)
                            .clip(MaterialTheme.shapes.small)
                    ) {
                        screensaver.animation(Modifier.fillMaxSize())
                    }
                    Text(
                        text = screensaver.displayName,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                Spacer(Modifier.size(64.dp))
            }
        }
    }
}

@Suppress("AssignedValueIsNeverRead")
@Preview
@Composable
private fun ScreensaverSettingsPreview() {
    ToucheTheme(darkTheme = false) {
        var screensaverEnabled by remember { mutableStateOf(true) }
        var currentScreensaver by remember { mutableStateOf(Screensavers.cloudy) }

        ScreensaverSettingsScreen(
            onBackPressed = {},
            onScreensaverToggle = { screensaverEnabled = it },
            onScreensaverChange = { currentScreensaver = it },
            screensaverEnabled = screensaverEnabled,
            screensaverAnimations = Screensavers.all(),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Suppress("AssignedValueIsNeverRead")
@Preview
@Composable
private fun ScreensaverSettingsDarkPreview() {
    ToucheTheme(darkTheme = true) {
        var screensaverEnabled by remember { mutableStateOf(true) }
        var currentScreensaver by remember { mutableStateOf(Screensavers.cloudy) }

        ScreensaverSettingsScreen(
            onBackPressed = {},
            onScreensaverToggle = { screensaverEnabled = it },
            onScreensaverChange = { currentScreensaver = it },
            screensaverEnabled = screensaverEnabled,
            screensaverAnimations = Screensavers.all(),
            modifier = Modifier.fillMaxSize()
        )
    }
}
