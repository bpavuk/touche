package dev.bpavuk.touche.ui.screens.home

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import dev.bpavuk.touche.R
import dev.bpavuk.touche.ui.components.MenuEntry
import dev.bpavuk.touche.ui.theme.ToucheTheme
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

const val SHARED_TRANSITION_SETTINGS_SCREEN_ID = "settings-screen"

@Composable
fun TimeBasedGreeting(modifier: Modifier = Modifier) {
    val text = if (LocalInspectionMode.current) {
        stringResource(R.string.good_evening)
    } else {
        val now = Clock.System.now()
        val systemTZ = TimeZone.currentSystemDefault()
        val thisTime = now.toLocalDateTime(systemTZ)

        when (thisTime.hour) {
            in 6..10 -> stringResource(R.string.good_morning)
            in 11..17 -> stringResource(R.string.have_a_great_day)
            in 18..22 -> stringResource(R.string.good_evening)
            else -> stringResource(R.string.what_a_night_huh)
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
            text = stringResource(R.string.ready_to_connect),
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
            Text(stringResource(R.string.settings), style = MaterialTheme.typography.titleMedium)
            Text(stringResource(R.string.make_touche_yours), style = MaterialTheme.typography.labelMedium)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToSettings: () -> Unit,
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope
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
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(0.4f))
            ReadyToConnect(
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.size(32.dp))
            with(sharedTransitionScope) {
                SettingsButton(
                    navigateToSettings = navigateToSettings,
                    modifier = Modifier
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(
                                SHARED_TRANSITION_SETTINGS_SCREEN_ID
                            ),
                            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                            resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds()
                        )
                        .clip(MaterialTheme.shapes.large)
                )
            }
            Spacer(Modifier.weight(0.6f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ToucheTheme(darkTheme = false) {
        SharedTransitionLayout {
            HomeScreen(
                navigateToSettings = {},
                modifier = Modifier.fillMaxSize(),
                sharedTransitionScope = this
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenDarkPreview() {
    ToucheTheme(darkTheme = true) {
        SharedTransitionLayout {
            HomeScreen(
                navigateToSettings = {},
                modifier = Modifier.fillMaxSize(),
                sharedTransitionScope = this
            )
        }
    }
}

