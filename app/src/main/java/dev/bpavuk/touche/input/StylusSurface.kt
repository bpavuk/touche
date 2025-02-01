package dev.bpavuk.touche.input

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import dev.bpavuk.touche.input.types.ScreenSize

@Composable
fun StylusSurface(viewModel: InputViewModel, modifier: Modifier = Modifier) {
    var size = IntSize.Zero

    Box(modifier then Modifier
        .onGloballyPositioned {
            size = it.size
        }
        .pointerInput(Unit) {
            this.awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    viewModel.pass(event.changes,
                        size.toSize()
                            .run { ScreenSize((width).toInt(), (height).toInt()) })
                }
            }
        }
    )
}
