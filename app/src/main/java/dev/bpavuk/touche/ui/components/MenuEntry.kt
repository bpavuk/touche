package dev.bpavuk.touche.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bpavuk.touche.R
import dev.bpavuk.touche.ui.theme.ToucheTheme

data class MenuEntryColors(
    val background: Color, val iconBackground: Color
) {

    companion object {
        @Composable
        fun default(): MenuEntryColors {
            return MenuEntryColors(
                background = MaterialTheme.colorScheme.primaryContainer,
                iconBackground = MaterialTheme.colorScheme.surfaceContainer
            )
        }
    }
}

@Composable
fun MenuEntry(
    leadIcon: Painter,
    trailingIcon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: MenuEntryColors = MenuEntryColors.default(),
    content: @Composable () -> Unit
) {
    Row(
        modifier
            .clickable(onClick = onClick)
            .background(colors.background)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = colors.iconBackground, shape = CircleShape
                )
                .padding(16.dp)
        ) {
            Icon(leadIcon, null)
        }
        Spacer(modifier = Modifier.size(16.dp))
        Box(Modifier.weight(1f)) {
            content()
        }
        Spacer(modifier = Modifier.size(16.dp))
        Icon(trailingIcon, null)
    }
}

@Preview
@Composable
private fun MenuEntryPreview() {
    ToucheTheme {
        MenuEntry(
            leadIcon = painterResource(R.drawable.settings_24px),
            trailingIcon = painterResource(R.drawable.chevron_right_24px),
            onClick = {}) {
            Column {
                Text("Lorem ipsum dolor sit amet")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
private fun MenuEntryListPreview() {
    ToucheTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val scrollBehavior = enterAlwaysScrollBehavior()
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    LargeFlexibleTopAppBar(
                        title = {
                            Text("touché")
                        },
                        scrollBehavior = scrollBehavior,
                    )
                }) { paddingValues ->
                val firstShape = MaterialTheme.shapes.small.copy(
                    topStart = MaterialTheme.shapes.largeIncreased.topStart,
                    topEnd = MaterialTheme.shapes.largeIncreased.topEnd,
                )
                val lastShape = MaterialTheme.shapes.small.copy(
                    bottomStart = MaterialTheme.shapes.largeIncreased.bottomStart,
                    bottomEnd = MaterialTheme.shapes.largeIncreased.bottomEnd,
                )

                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                ) {
                    repeat(50) {
                        item(key = it) {
                            MenuEntry(
                                leadIcon = painterResource(R.drawable.settings_24px),
                                trailingIcon = painterResource(R.drawable.chevron_right_24px),
                                onClick = {},
                                modifier = Modifier
                                    .padding(1.dp)
                                    .clip(
                                        shape = when (it) {
                                            0 -> firstShape

                                            49 -> lastShape

                                            else -> MaterialTheme.shapes.small
                                        }
                                    )
                            ) {
                                Column {
                                    Text("Lorem ipsum dolor sit amet")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}