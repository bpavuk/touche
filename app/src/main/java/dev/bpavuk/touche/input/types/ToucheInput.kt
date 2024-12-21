package dev.bpavuk.touche.input.types

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerType

sealed interface ToucheInput {
    val offset: Offset

    data class Stylus(
        override val offset: Offset,
        val pressed: Boolean,
        val pressure: Float
    ): ToucheInput
    data class Finger(override val offset: Offset): ToucheInput
}

fun ToucheInput.toPointerType(): PointerType {
    return when (this) {
        is ToucheInput.Finger -> PointerType.Touch
        is ToucheInput.Stylus -> PointerType.Stylus
    }
}

fun PointerInputChange.toToucheInput(): ToucheInput {
    return when (this.type) {
        PointerType.Stylus -> ToucheInput.Stylus(position, pressed, pressure)
        else -> { ToucheInput.Finger(position) }
    }
}
