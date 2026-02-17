package dev.bpavuk.touche.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import dev.bpavuk.touche.logic.input.InputViewModel
import dev.bpavuk.touche.ui.preview.MultiDevicePreview
import dev.bpavuk.touche.ui.surfaces.Cloudy
import dev.bpavuk.touche.ui.theme.ToucheTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import dev.bpavuk.touche.ui.screens.settings.Screensaver

@Composable
fun StylusSurface(viewModel: InputViewModel, modifier: Modifier = Modifier) {
    val stylusEnabled by viewModel.getStylusEnabled().collectAsState(false)
    val touchpadEnabled by viewModel.getTouchpadEnabled().collectAsState(false)
    val screensaverEnabled by viewModel.getScreensaverEnabled().collectAsState(false)

    Box(modifier then Modifier
        .onGloballyPositioned {
            viewModel.sendScreenEvent(it.size)
        }
        .pointerInput(Unit) {
            this.awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    viewModel.sendPointerEvent(
                        inputChange = event.changes,
                        stylusEnabled,
                        touchpadEnabled
                    )
                }
            }
        }) {
        if (screensaverEnabled) {
            Cloudy(Modifier.fillMaxSize())
        }
    }
}

@MultiDevicePreview
@Preview
@Composable
fun StylusSurfacePreview() {
    val dumbViewModel = object : InputViewModel {
        override fun sendPointerEvent(
            inputChange: List<PointerInputChange>,
            stylusEnabled: Boolean,
            touchpadEnabled: Boolean
        ) {

        }

        override fun sendScreenEvent(screenSize: IntSize) {

        }

        override fun getStylusEnabled(): Flow<Boolean> {
            return flowOf(false)
        }

        override fun getTouchpadEnabled(): Flow<Boolean> {
            return flowOf(false)
        }

        override fun getScreensaverEnabled(): Flow<Boolean> {
            return flowOf(false)
        }

        override fun getScreensaver(): Flow<Screensaver?> {
            return flowOf(null)
        }
    }
    ToucheTheme {
        StylusSurface(
            dumbViewModel, modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        )
    }
}