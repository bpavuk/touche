package dev.bpavuk.touche.input.types

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerType

sealed interface ToucheInput {
    val offset: Offset
    val screenSize: ScreenSize

    data class Stylus(
        override val offset: Offset,
        val pressed: Boolean,
        val pressure: Float,
        override val screenSize: ScreenSize
    ) : ToucheInput

    data class Finger(
        override val offset: Offset,
        val pressed: Boolean,
        override val screenSize: ScreenSize,
        val id: PointerId
    ) : ToucheInput
}

data class ScreenSize(val x: Int, val y: Int)

fun PointerInputChange.toToucheInput(screenSize: ScreenSize): ToucheInput {
    return when (this.type) {
        PointerType.Stylus -> ToucheInput.Stylus(
            position,
            pressed,
            pressure,
            screenSize
        )

        PointerType.Touch -> {
            ToucheInput.Finger(position, pressed, screenSize, id)
        }

        else -> throw IllegalStateException()
    }
}
