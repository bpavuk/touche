package dev.bpavuk.touche.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bpavuk.touche.R
import dev.bpavuk.touche.ui.components.MenuEntry
import dev.bpavuk.touche.ui.theme.ToucheTheme
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@Composable
fun TimeBasedGreeting(modifier: Modifier = Modifier) {
    val text = if (LocalInspectionMode.current) {
        "Good evening"
    } else {
        remember {
            val now = Clock.System.now()
            val systemTZ = TimeZone.currentSystemDefault()
            val thisTime = now.toLocalDateTime(systemTZ)

            when (thisTime.hour) {
                in 6..10 -> "Good morning"
                in 11..17 -> "Have a great day"
                in 18..22 -> "Good evening"
                else -> "What a night, huh?"
            }
        }
    }

    Text(
        text,
        modifier,
        style = MaterialTheme.typography.headlineLarge
    )
}

@Composable
fun ConnectionArt(modifier: Modifier = Modifier) {
    Box(modifier) {
        // TODO
    }
}

@Composable
fun ReadyToConnect(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.extraLarge
            )
            .padding(16.dp)
                then modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConnectionArt(
            modifier = Modifier
                .defaultMinSize(minHeight = 200.dp)
                .fillMaxWidth()
        )
        Text(
            text = "Ready to connect!",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
fun SettingsButton(
    navigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    MenuEntry(
        leadIcon = painterResource(R.drawable.settings_24px),
        onClick = navigateToSettings,
        trailingIcon = painterResource(R.drawable.chevron_right_24px),
        modifier = modifier
    ) {
        Column {
            Text("Settings", style = MaterialTheme.typography.titleMedium)
            Text("Make touché yours", style = MaterialTheme.typography.labelMedium)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TimeBasedGreeting()
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(0.4f))
            ReadyToConnect(
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.size(32.dp))
            SettingsButton(
                navigateToSettings = navigateToSettings,
                modifier = Modifier.clip(MaterialTheme.shapes.large)
            )
            Spacer(Modifier.weight(0.6f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ToucheTheme(darkTheme = false) {
        HomeScreen(
            navigateToSettings = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenDarkPreview() {
    ToucheTheme(darkTheme = true) {
        HomeScreen(
            navigateToSettings = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

