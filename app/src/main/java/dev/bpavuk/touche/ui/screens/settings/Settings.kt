package dev.bpavuk.touche.ui.screens.settings

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import dev.bpavuk.touche.R
import dev.bpavuk.touche.ui.components.BackButton
import dev.bpavuk.touche.ui.components.MenuEntry
import dev.bpavuk.touche.ui.screens.home.SHARED_TRANSITION_SETTINGS_SCREEN_ID
import dev.bpavuk.touche.ui.theme.ToucheTheme


const val SHARED_TRANSITION_SCREENSAVER_SCREEN_ID = "screensaver-screen"
const val SHARED_TRANSITION_TOUCHPAD_SCREEN_ID = "touchpad-screen"
const val SHARED_TRANSITION_STYLUS_SCREEN_ID = "stylus-screen"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsScreen(
    onBackPressed: () -> Unit,
    navigateToScreensaver: () -> Unit,
    navigateToTouchpad: () -> Unit,
    navigateToStylus: () -> Unit,
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            with(sharedTransitionScope) {
                LargeFlexibleTopAppBar(
                    title = {
                        Text(stringResource(R.string.settings))
                    },
                    navigationIcon = {
                        BackButton(onBackPressed)
                    },
                    expandedHeight = 200.dp,
                    scrollBehavior = scrollBehavior,
                    modifier = Modifier.sharedBounds(
                        sharedContentState = rememberSharedContentState(
                            key = SHARED_TRANSITION_SETTINGS_SCREEN_ID
                        ),
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current
                    )
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(paddingValues)
                .padding(24.dp)
                .fillMaxSize()
        ) {
            item {
                with(sharedTransitionScope) {
                    MenuEntry(
                        leadIcon = painterResource(R.drawable.mobile_screensaver_24px),
                        trailingIcon = painterResource(R.drawable.chevron_right_24px),
                        onClick = navigateToScreensaver,
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(
                                    SHARED_TRANSITION_SCREENSAVER_SCREEN_ID
                                ),
                                animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                                resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds()
                            )
                            .clip(
                                MaterialTheme.shapes.small
                                    .copy(
                                        topStart = MaterialTheme.shapes.extraLarge.topStart,
                                        topEnd = MaterialTheme.shapes.extraLarge.topEnd
                                    )
                            )
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.screensaver),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(Modifier.size(4.dp))
                            Text(
                                text = stringResource(R.string.screensaver_description),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
                Spacer(Modifier.size(4.dp))
            }
            item {
                with(sharedTransitionScope) {
                    MenuEntry(
                        leadIcon = painterResource(R.drawable.touchpad_mouse_24px),
                        trailingIcon = painterResource(R.drawable.chevron_right_24px),
                        onClick = navigateToTouchpad,
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(
                                    SHARED_TRANSITION_TOUCHPAD_SCREEN_ID
                                ),
                                animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                                resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds()
                            )
                            .clip(MaterialTheme.shapes.small)
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.touchpad),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(Modifier.size(4.dp))
                            Text(
                                text = stringResource(R.string.touchpad_settings_description),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
                Spacer(Modifier.size(4.dp))
            }
            item {
                with(sharedTransitionScope) {
                    MenuEntry(
                        leadIcon = painterResource(R.drawable.stylus_24px),
                        trailingIcon = painterResource(R.drawable.chevron_right_24px),
                        onClick = navigateToStylus,
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(
                                    SHARED_TRANSITION_STYLUS_SCREEN_ID
                                ),
                                animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                                resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds()
                            )
                            .clip(
                                MaterialTheme.shapes.small
                                    .copy(
                                        bottomStart = MaterialTheme.shapes.extraLarge.bottomStart,
                                        bottomEnd = MaterialTheme.shapes.extraLarge.bottomEnd
                                    )
                            )
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.stylus),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(Modifier.size(4.dp))
                            Text(
                                text = stringResource(R.string.stylus_settings_description),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    ToucheTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            SharedTransitionLayout {
                SettingsScreen(
                    onBackPressed = {},
                    navigateToScreensaver = {},
                    navigateToTouchpad = {},
                    navigateToStylus = {},
                    modifier = Modifier.fillMaxSize(),
                    sharedTransitionScope = this
                )
            }
        }
    }
}

@Preview
@Composable
private fun SettingsScreenDarkPreview() {
    ToucheTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            SharedTransitionLayout {
                SettingsScreen(
                    onBackPressed = {},
                    navigateToScreensaver = {},
                    navigateToTouchpad = {},
                    navigateToStylus = {},
                    modifier = Modifier.fillMaxSize(),
                    sharedTransitionScope = this
                )
            }
        }
    }
}
