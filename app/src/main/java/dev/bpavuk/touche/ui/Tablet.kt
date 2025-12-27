package dev.bpavuk.touche.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import dev.bpavuk.touche.input.InputViewModel
import dev.bpavuk.touche.ui.preview.MultiDevicePreview
import dev.bpavuk.touche.ui.theme.ToucheTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Tablet(
    inputViewModel: InputViewModel,
    modifier: Modifier = Modifier
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val useRow = windowSizeClass
        .isWidthAtLeastBreakpoint(
            widthDpBreakpoint = WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
        )

    val surface: @Composable (Modifier) -> Unit = { modifier ->
        StylusSurface(
            viewModel = inputViewModel,
            modifier = modifier
                .background(Color.Black)
        )

    }

    if (useRow) {
        Row(modifier) {
            surface(Modifier.weight(1f))
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(40.dp)
            ) {
                repeat(5) { buttonNumber ->
                    Button(onClick = {}, modifier = Modifier.weight(1f)) { }
                    if (buttonNumber < 4) {
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
    } else {
        Column(modifier) {
            surface(Modifier.weight(1f))
            ButtonGroup(
                overflowIndicator = {}
            ) {
                repeat(5) {
                    clickableItem(onClick = {}, weight = 1f, label = "")
                }
            }
        }
    }
}

@MultiDevicePreview
@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
private fun TabletPreview() {
    val inputViewModel = object : InputViewModel {
        override fun sendPointerEvent(inputChange: List<PointerInputChange>) {
        }

        override fun sendScreenEvent(screenSize: IntSize) {
        }

    }
    ToucheTheme(darkTheme = true) {
        Tablet(inputViewModel, modifier = Modifier.fillMaxSize())
    }
}
