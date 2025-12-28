package dev.bpavuk.touche.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bpavuk.touche.R
import dev.bpavuk.touche.ui.components.BackButton
import dev.bpavuk.touche.ui.components.MenuEntry
import dev.bpavuk.touche.ui.theme.ToucheTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsScreen(
    onBackPressed: () -> Unit,
    navigateToScreensaver: () -> Unit,
    navigateToTouchpad: () -> Unit,
    navigateToStylus: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            LargeFlexibleTopAppBar(
                title = {
                    Text("Settings") // TODO: replace with string res
                },
                navigationIcon = {
                    BackButton(onBackPressed)
                },
                expandedHeight = 200.dp,
                scrollBehavior = scrollBehavior
            )
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
                MenuEntry(
                    leadIcon = painterResource(R.drawable.mobile_screensaver_24px),
                    trailingIcon = painterResource(R.drawable.chevron_right_24px),
                    onClick = navigateToScreensaver,
                    modifier = Modifier.clip(
                        MaterialTheme.shapes.small
                            .copy(
                                topStart = MaterialTheme.shapes.extraLarge.topStart,
                                topEnd = MaterialTheme.shapes.extraLarge.topEnd
                            )
                    )
                ) {
                    Column {
                        Text(
                            text = "Screensaver",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.size(4.dp))
                        Text(
                            text = "An animation displayed when touché is connected",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                Spacer(Modifier.size(4.dp))
            }
            item {
                MenuEntry(
                    leadIcon = painterResource(R.drawable.touchpad_mouse_24px),
                    trailingIcon = painterResource(R.drawable.chevron_right_24px),
                    onClick = navigateToTouchpad,
                    modifier = Modifier.clip(MaterialTheme.shapes.small)
                ) {
                    Column {
                        Text(
                            text = "Touchpad",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.size(4.dp))
                        Text(
                            text = "Settings specific to the touchpad functionality",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                Spacer(Modifier.size(4.dp))
            }
            item {
                MenuEntry(
                    leadIcon = painterResource(R.drawable.stylus_24px),
                    trailingIcon = painterResource(R.drawable.chevron_right_24px),
                    onClick = navigateToStylus,
                    modifier = Modifier.clip(
                        MaterialTheme.shapes.small
                            .copy(
                                bottomStart = MaterialTheme.shapes.extraLarge.bottomStart,
                                bottomEnd = MaterialTheme.shapes.extraLarge.bottomEnd
                            )
                    )
                ) {
                    Column {
                        Text(
                            text = "Stylus",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.size(4.dp))
                        Text(
                            text = "For configuring the stylus / graphics tablet behavior",
                            style = MaterialTheme.typography.labelSmall
                        )
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
            SettingsScreen(
                onBackPressed = {},
                navigateToStylus = {},
                navigateToTouchpad = {},
                navigateToScreensaver = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
private fun SettingsScreenDarkPreview() {
    ToucheTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            SettingsScreen(
                onBackPressed = {},
                navigateToStylus = {},
                navigateToTouchpad = {},
                navigateToScreensaver = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
