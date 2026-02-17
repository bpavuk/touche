package dev.bpavuk.touche

import android.content.Intent
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
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import dev.bpavuk.touche.data.persistence.OnboardingRepository
import dev.bpavuk.touche.logic.settings.ScreensaverSettingsViewModelImpl
import dev.bpavuk.touche.logic.settings.StylusSettingsViewModelImpl
import dev.bpavuk.touche.logic.settings.TouchpadSettingsViewModelImpl
import dev.bpavuk.touche.ui.screens.home.HomeScreen
import dev.bpavuk.touche.ui.screens.onboarding.OnboardingScreen
import dev.bpavuk.touche.ui.screens.settings.Screensaver
import dev.bpavuk.touche.ui.screens.settings.ScreensaverSettingsScreen
import dev.bpavuk.touche.ui.screens.settings.Screensavers
import dev.bpavuk.touche.ui.screens.settings.SettingsScreen
import dev.bpavuk.touche.ui.screens.settings.StylusScreen
import dev.bpavuk.touche.ui.screens.settings.TouchpadScreen
import dev.bpavuk.touche.ui.theme.ToucheTheme
import dev.bpavuk.touche.ui.theme.forwardMovementSpec
import dev.bpavuk.touche.ui.theme.popMovementSpec
import dev.bpavuk.touche.ui.theme.predictivePopMovementSpec
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
    val backStack = rememberNavBackStack(startRoute)

    val touchpadViewModel = koinViewModel<TouchpadSettingsViewModelImpl>()
    val screensaverViewModel = koinViewModel<ScreensaverSettingsViewModelImpl>()
    val stylusViewModel = koinViewModel<StylusSettingsViewModelImpl>()
    val onboardingRepository = koinInject<OnboardingRepository>()

    val touchpadEnabled by touchpadViewModel.getTouchpadEnabled().collectAsState(initial = true)
    val stylusEnabled by stylusViewModel.getStylusEnabled().collectAsState(initial = true)
    val screensaverEnabled by screensaverViewModel.getScreensaverEnabled().collectAsState(initial = true)
    val screensaverId by screensaverViewModel.getScreensaverId().collectAsState(initial = Screensavers.cloudy.id)

    val currentScreensaver = remember(screensaverId) {
        Screensavers.all().find { it.id == screensaverId } ?: Screensavers.cloudy
    }

    val availableScreensavers = remember { Screensavers.all() }
    val screensaverOptions: List<Screensaver> = remember(currentScreensaver, availableScreensavers) {
        buildList {
            add(currentScreensaver)
            addAll(availableScreensavers.filterNot { it.id == currentScreensaver.id })
        }
    }

    val coroutineScope = rememberCoroutineScope()

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        transitionSpec = forwardMovementSpec,
        popTransitionSpec = popMovementSpec,
        predictivePopTransitionSpec = predictivePopMovementSpec,
        entryProvider = entryProvider {
            entry<OnboardingRoute> {
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
                    modifier = Modifier.fillMaxSize(),
                )
            }

            entry<HomeRoute> {
                HomeScreen(
                    navigateToSettings = { backStack.add(SettingsRoute) },
                    modifier = Modifier.fillMaxSize(),
                )
            }

            entry<SettingsRoute> {
                SettingsScreen(
                    onBackPressed = { backStack.removeLastOrNull() },
                    navigateToScreensaver = { backStack.add(ScreensaverRoute) },
                    navigateToTouchpad = { backStack.add(TouchpadRoute) },
                    navigateToStylus = { backStack.add(StylusRoute) },
                    modifier = Modifier.fillMaxSize(),
                )
            }

            entry<ScreensaverRoute> {
                ScreensaverSettingsScreen(
                    onBackPressed = { backStack.removeLastOrNull() },
                    onScreensaverToggle = { screensaverViewModel.setScreensaverEnabled(it) },
                    onScreensaverChange = { screensaverViewModel.setScreensaverId(it.id) },
                    screensaverEnabled = screensaverEnabled,
                    screensaverAnimations = screensaverOptions,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            entry<TouchpadRoute> {
                TouchpadScreen(
                    onBackPressed = { backStack.removeLastOrNull() },
                    onTouchpadToggle = { touchpadViewModel.setTouchpadEnabled(it) },
                    touchpadEnabled = touchpadEnabled,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            entry<StylusRoute> {
                StylusScreen(
                    onBackPressed = { backStack.removeLastOrNull() },
                    onStylusToggle = { stylusViewModel.setStylusEnabled(it) },
                    stylusEnabled = stylusEnabled,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        },
        modifier = modifier.background(MaterialTheme.colorScheme.background),
    )
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
