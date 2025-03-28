package dev.bpavuk.touche.input

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned

@Composable
fun StylusSurface(viewModel: InputViewModel, modifier: Modifier = Modifier) {
    Box(modifier then Modifier
        .onGloballyPositioned {
            viewModel.setScreenState(it.size)
        }
        .pointerInput(Unit) {
            this.awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    viewModel.pass(event.changes)
                }
            }
        }
    )
}
