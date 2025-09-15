package dev.bpavuk.touche.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import dev.bpavuk.touche.input.InputViewModel

@Composable
fun StylusSurface(viewModel: InputViewModel, modifier: Modifier = Modifier.Companion) {
    Box(
        modifier then Modifier.Companion
        .onGloballyPositioned {
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
    )
}