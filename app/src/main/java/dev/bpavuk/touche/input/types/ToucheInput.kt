package dev.bpavuk.touche.input.types

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerId

sealed interface ToucheInput : ToucheData {
    val offset: Offset
    val screenSize: ToucheScreenSize

    data class Stylus(
        override val offset: Offset,
        val pressed: Boolean,
        val pressure: Float,
        override val screenSize: ToucheScreenSize
    ) : ToucheInput {
        override fun encode(): ByteArray = buildString {
            append("S\t")
            append("${offset.x}\t")
            append("${offset.y}\t")
            append(if (pressed) "1\t${pressure}" else "0")
        }.encodeToByteArray()
    }

    data class Finger(
        override val offset: Offset,
        val pressed: Boolean,
        override val screenSize: ToucheScreenSize,
        val id: PointerId
    ) : ToucheInput {
        override fun encode(): ByteArray = buildString {
            append("F\t")
            append("${offset.x}\t")
            append("${offset.y}\t")
            append(if (pressed) "1\t" else "0\t")
            append(id.value)
        }.encodeToByteArray()
    }
}
