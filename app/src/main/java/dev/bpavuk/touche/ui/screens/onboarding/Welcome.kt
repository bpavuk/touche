package dev.bpavuk.touche.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bpavuk.touche.R
import dev.bpavuk.touche.ui.theme.ToucheTheme

@Composable
fun OnboardingArtWelcome(modifier: Modifier = Modifier) {
    Box(modifier.background(Color.LightGray)) {
        // TODO
    }
}

@Composable
fun OnboardingArtDriver(modifier: Modifier = Modifier) {
    Box(modifier.background(Color.LightGray)) {
        // TODO
    }
}

@Composable
private fun Welcome(
    onCompletion: () -> Unit,
    modifier: Modifier = Modifier
) {
    // TODO: make it adaptive
    Column(modifier) {
        OnboardingArtWelcome(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Column(modifier = Modifier.padding(32.dp)) {
            Text("Welcome!", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            Text(
                "touché is an app that connects your Android device as a graphics tablet and " +
                        "touchpad to your Linux desktop."
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Spacer(Modifier.weight(1f))
            Button(onClick = onCompletion) {
                Text("Continue")
            }
        }
    }
}

@Preview
@Composable
private fun WelcomePreview() {
    ToucheTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Welcome(onCompletion = {}, modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun Driver(
    onCompletion: () -> Unit,
    downloadDriver: () -> Unit,
    modifier: Modifier = Modifier
) {
    // TODO: make it adaptive
    Column(modifier) {
        OnboardingArtDriver(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Column(modifier = Modifier.padding(32.dp)) {
            Text(
                text = "You will need a desktop app",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(16.dp))
            Text(text = "To use this app, you should pair it with a desktop. " +
                    "Here is the link to download it.")
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Button(onClick = downloadDriver) {
                Text("Download")
            }
            Spacer(Modifier.weight(1f))
            OutlinedButton(onClick = onCompletion) {
                Text("I'm ready")
            }
        }
    }
}


@Preview
@Composable
private fun DriverPreview() {
    ToucheTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Driver(
                onCompletion = {},
                downloadDriver = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

sealed interface OnboardingScreenState {
    data object Welcome : OnboardingScreenState
    data object Driver : OnboardingScreenState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onCompletion: () -> Unit,
    onDriverDownload: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            var state: OnboardingScreenState by retain {
                mutableStateOf(OnboardingScreenState.Welcome)
            }

            when (state) {
                OnboardingScreenState.Welcome -> Welcome(onCompletion = {
                    state = OnboardingScreenState.Driver
                })

                OnboardingScreenState.Driver -> Driver(
                    onCompletion,
                    downloadDriver = onDriverDownload
                )
            }
        }
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    ToucheTheme {
        OnboardingScreen(
            onCompletion = {},
            onDriverDownload = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}