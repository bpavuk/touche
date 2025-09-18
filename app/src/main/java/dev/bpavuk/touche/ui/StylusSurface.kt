package dev.bpavuk.touche.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import dev.bpavuk.touche.input.InputViewModel
import dev.bpavuk.touche.ui.preview.MultiDevicePreview
import dev.bpavuk.touche.ui.surfaces.Cloudy
import dev.bpavuk.touche.ui.theme.ToucheTheme

@Composable
fun StylusSurface(viewModel: InputViewModel, modifier: Modifier = Modifier) {
    Box(
        modifier then Modifier.onGloballyPositioned {
            viewModel.sendScreenEvent(it.size)
        }
        .pointerInput(Unit) {
            this.awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    viewModel.sendPointerEvent(event.changes)
                }
            }
        }
    ) {
        Cloudy(Modifier.fillMaxSize())
    }
}

@MultiDevicePreview
@Preview
@Composable
fun StylusSurfacePreview() {
    val dumbViewModel = object : InputViewModel {
        override fun sendPointerEvent(inputChange: List<PointerInputChange>) {

        }

        override fun sendScreenEvent(screenSize: IntSize) {

        }
    }
    ToucheTheme {
        StylusSurface(dumbViewModel, modifier = Modifier.fillMaxSize().background(Color.Black))
    }
}