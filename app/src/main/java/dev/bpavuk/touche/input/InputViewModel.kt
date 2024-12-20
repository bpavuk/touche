package dev.bpavuk.touche.input

import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.lifecycle.ViewModel
import dev.bpavuk.touche.input.types.toToucheInput


class InputViewModel(private val handler: InputHandler) : ViewModel() {
    fun pass(inputChange: PointerInputChange) {
        handler.pass(inputChange.toToucheInput())
    }
}