package dev.bpavuk.touche

import android.content.Intent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import dev.bpavuk.touche.data.persistence.OnboardingRepository
import dev.bpavuk.touche.logic.settings.ScreensaverSettingsViewModelImpl
import dev.bpavuk.touche.logic.settings.StylusSettingsViewModelImpl
import dev.bpavuk.touche.logic.settings.TouchpadSettingsViewModelImpl
import dev.bpavuk.touche.ui.screens.home.HomeScreen
import dev.bpavuk.touche.ui.screens.home.SHARED_TRANSITION_SETTINGS_SCREEN_ID
import dev.bpavuk.touche.ui.screens.onboarding.OnboardingScreen
import dev.bpavuk.touche.ui.screens.settings.SHARED_TRANSITION_SCREENSAVER_SCREEN_ID
import dev.bpavuk.touche.ui.screens.settings.SHARED_TRANSITION_STYLUS_SCREEN_ID
import dev.bpavuk.touche.ui.screens.settings.SHARED_TRANSITION_TOUCHPAD_SCREEN_ID
import dev.bpavuk.touche.ui.screens.settings.Screensaver
import dev.bpavuk.touche.ui.screens.settings.ScreensaverSettingsScreen
import dev.bpavuk.touche.ui.screens.settings.Screensavers
import dev.bpavuk.touche.ui.screens.settings.SettingsScreen
import dev.bpavuk.touche.ui.screens.settings.StylusScreen
import dev.bpavuk.touche.ui.screens.settings.TouchpadScreen
import dev.bpavuk.touche.ui.theme.ToucheTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val DRIVER_REPO_URL = "https://github.com/bpavuk/touche-driver"

private sealed interface AppRoute : NavKey

@Serializable
private data object HomeRoute : AppRoute

@Serializable
private data object OnboardingRoute : AppRoute

@Serializable
private data object SettingsRoute : AppRoute

@Serializable
private data object ScreensaverRoute : AppRoute

@Serializable
private data object TouchpadRoute : AppRoute

@Serializable
private data object StylusRoute : AppRoute

@Composable
fun AppRoot(
    modifier: Modifier = Modifier,
    onboard: Boolean = false,
) {
    val context = LocalContext.current
    val startRoute: AppRoute = if (onboard) OnboardingRoute else HomeRoute

    // we are only allowing AppRoute-based keys here. jerks at Google barely know anything about
    // type safety
    @Suppress("UNCHECKED_CAST")
    val backStack: NavBackStack<AppRoute> =
        rememberNavBackStack(startRoute) as NavBackStack<AppRoute>

    val touchpadViewModel = koinViewModel<TouchpadSettingsViewModelImpl>()
    val screensaverViewModel = koinViewModel<ScreensaverSettingsViewModelImpl>()
    val stylusViewModel = koinViewModel<StylusSettingsViewModelImpl>()
    val onboardingRepository = koinInject<OnboardingRepository>()

    val touchpadEnabled by touchpadViewModel.getTouchpadEnabled().collectAsState(initial = true)
    val stylusEnabled by stylusViewModel.getStylusEnabled().collectAsState(initial = true)
    val screensaverEnabled by screensaverViewModel.getScreensaverEnabled()
        .collectAsState(initial = true)
    val screensaverId by screensaverViewModel.getScreensaverId()
        .collectAsState(initial = Screensavers.cloudy.id)

    val currentScreensaver = remember(screensaverId) {
        Screensavers.all().find { it.id == screensaverId } ?: Screensavers.cloudy
    }

    val availableScreensavers = remember { Screensavers.all() }
    val screensaverOptions: List<Screensaver> =
        remember(currentScreensaver, availableScreensavers) {
            buildList {
                add(currentScreensaver)
                addAll(availableScreensavers.filterNot { it.id == currentScreensaver.id })
            }
        }

    val coroutineScope = rememberCoroutineScope()

    SharedTransitionLayout {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            modifier = modifier.background(MaterialTheme.colorScheme.background),
        ) { key ->
            when (key) {
                OnboardingRoute -> NavEntry(key) {
                    OnboardingScreen(
                        onCompletion = {
                            coroutineScope.launch {
                                onboardingRepository.setOnboardingCompleted(true)
                            }
                            backStack.clear()
                            backStack.add(HomeRoute)
                        },
                        onDriverDownload = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, DRIVER_REPO_URL)
                            }
                            context.startActivity(Intent.createChooser(shareIntent, null))
                        },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                HomeRoute -> NavEntry(key) {
                    HomeScreen(
                        navigateToSettings = { backStack.add(SettingsRoute) },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                SettingsRoute -> NavEntry(key) {
                    SettingsScreen(
                        onBackPressed = { backStack.removeLastOrNull() },
                        navigateToScreensaver = { backStack.add(ScreensaverRoute) },
                        navigateToTouchpad = { backStack.add(TouchpadRoute) },
                        navigateToStylus = { backStack.add(StylusRoute) },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                ScreensaverRoute -> NavEntry(key) {
                    ScreensaverSettingsScreen(
                        onBackPressed = { backStack.removeLastOrNull() },
                        onScreensaverToggle = { screensaverViewModel.setScreensaverEnabled(it) },
                        onScreensaverChange = { screensaverViewModel.setScreensaverId(it.id) },
                        screensaverEnabled = screensaverEnabled,
                        screensaverAnimations = screensaverOptions,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        modifier = Modifier
                            .fillMaxSize(),
                    )
                }

                TouchpadRoute -> NavEntry(key) {
                    TouchpadScreen(
                        onBackPressed = { backStack.removeLastOrNull() },
                        onTouchpadToggle = { touchpadViewModel.setTouchpadEnabled(it) },
                        touchpadEnabled = touchpadEnabled,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        modifier = Modifier
                            .fillMaxSize(),
                    )
                }

                StylusRoute -> NavEntry(key) {
                    StylusScreen(
                        onBackPressed = { backStack.removeLastOrNull() },
                        onStylusToggle = { stylusViewModel.setStylusEnabled(it) },
                        stylusEnabled = stylusEnabled,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        modifier = Modifier
                            .fillMaxSize(),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppRootPreview() {
    ToucheTheme(darkTheme = false) {
        AppRoot(modifier = Modifier.fillMaxSize())
    }
}

@Preview(showBackground = true)
@Composable
private fun AppRootDarkPreview() {
    ToucheTheme(darkTheme = true) {
        AppRoot(modifier = Modifier.fillMaxSize())
    }
}

@Preview(showBackground = true)
@Composable
private fun AppRootOnboardingPreview() {
    ToucheTheme(darkTheme = false) {
        AppRoot(
            modifier = Modifier.fillMaxSize(),
            onboard = true,
        )
    }
}
