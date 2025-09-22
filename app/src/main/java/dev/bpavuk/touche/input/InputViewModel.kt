package dev.bpavuk.touche.input

import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bpavuk.touche.data.ToucheRepository
import dev.bpavuk.touche.data.model.ToucheInput
import kotlinx.coroutines.launch

interface InputViewModel {
    fun sendPointerEvent(inputChange: List<PointerInputChange>)
    fun sendScreenEvent(screenSize: IntSize)
}

class InputViewModelImpl(
    private val client: ToucheRepository
) : InputViewModel, ViewModel() {
    override fun sendPointerEvent(inputChange: List<PointerInputChange>) {
        viewModelScope.launch {
            client.sendEvents(inputChange.mapNotNull {
                val input = it.toToucheInput()
                if (input != null) it.consume()
                input
            })
        }
    }

    override fun sendScreenEvent(screenSize: IntSize) {
        viewModelScope.launch {
            client.sendEvent(ToucheInput.Action.Init)
            client.sendEvent(screenSize.toToucheInput())
        }
    }

    private fun IntSize.toToucheInput(): ToucheInput =
        toSize().run { ToucheInput.Screen((width).toInt(), (height).toInt()) }

    private fun PointerInputChange.toToucheInput(): ToucheInput? {
        return when (this.type) {
            PointerType.Stylus -> ToucheInput.Stylus(
                position.x.toInt(), position.y.toInt(), pressed, pressure
            )

            PointerType.Touch -> {
                ToucheInput.Finger(position.x.toInt(), position.y.toInt(), pressed, id.value)
            }

            else -> null
        }
    }
}
