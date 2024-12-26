package dev.bpavuk.touche.input

import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bpavuk.touche.input.types.toToucheInput
import kotlinx.coroutines.launch


class InputViewModel(
    private val handler: InputHandler,
) : ViewModel() {
    fun pass(inputChange: PointerInputChange) {
        viewModelScope.launch {
            handler.pass(inputChange.toToucheInput())
        }
    }
}