package dev.bpavuk.touche.input

import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bpavuk.touche.connectivity.WatcherViewModel
import dev.bpavuk.touche.types.ToucheScreenSize
import dev.bpavuk.touche.types.ToucheInput
import kotlinx.coroutines.launch


class InputViewModel(
    private val watcher: WatcherViewModel,
) : ViewModel() {
    init {
        watcher.watch()
    }

    fun pass(inputChange: List<PointerInputChange>) {
        viewModelScope.launch {
            watcher.pass(inputChange.map {
                val input = it.toToucheInput()
                it.consume()
                input
            })
        }
    }

    fun setScreenState(screenSize: IntSize) {
        viewModelScope.launch {
            watcher.setScreenState(
                screenSize.toSize().run { ToucheScreenSize((width).toInt(), (height).toInt()) })
        }
    }
}

fun PointerInputChange.toToucheInput(): ToucheInput {
    return when (this.type) {
        PointerType.Stylus -> ToucheInput.Stylus(
            position, pressed, pressure
        )

        PointerType.Touch -> {
            ToucheInput.Finger(position, pressed, id)
        }

        else -> throw IllegalStateException()
    }
}
