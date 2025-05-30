package dev.bpavuk.touche.types

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerId

sealed interface ToucheInput : ToucheData {
    val offset: Offset

    data class Stylus(
        override val offset: Offset,
        val pressed: Boolean,
        val pressure: Float,
    ) : ToucheInput {
        override fun encode(): String = buildString {
            append("S\t")
            append("${offset.x}\t")
            append("${offset.y}\t")
            append(if (pressed) "1\t${pressure}" else "0")
        }
    }

    data class Finger(
        override val offset: Offset,
        val pressed: Boolean,
        val id: PointerId
    ) : ToucheInput {
        override fun encode(): String {
            return buildString {
                append("F\t")
                append("${offset.x}\t")
                append("${offset.y}\t")
                append(if (pressed) "1\t" else "0\t")
                append(id.value)
            }
        }
    }
}
