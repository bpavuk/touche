package dev.bpavuk.touche.input

import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bpavuk.touche.input.types.ToucheScreenSize
import dev.bpavuk.touche.input.types.ToucheInput
import kotlinx.coroutines.launch


class InputViewModel(
    private val watcher: WatcherViewModel,
) : ViewModel() {
    init {
        watcher.watch()
    }

    fun pass(inputChange: List<PointerInputChange>, screenSize: ToucheScreenSize) {
        viewModelScope.launch {
            watcher.pass(inputChange.map {
                it.consume()
                it.toToucheInput(screenSize)
            })
        }
    }

    fun setScreenState(screenSize: IntSize) {
        watcher.setScreenState(
            screenSize.toSize().run { ToucheScreenSize((width).toInt(), (height).toInt()) })
    }
}

fun PointerInputChange.toToucheInput(screenSize: ToucheScreenSize): ToucheInput {
    return when (this.type) {
        PointerType.Stylus -> ToucheInput.Stylus(
            position, pressed, pressure, screenSize
        )

        PointerType.Touch -> {
            ToucheInput.Finger(position, pressed, screenSize, id)
        }

        else -> throw IllegalStateException()
    }
}
