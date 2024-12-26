package dev.bpavuk.touche.input

import android.util.Log
import dev.bpavuk.touche.input.types.ToucheInput
import dev.bpavuk.touche.input.types.toPointerType
import dev.bpavuk.touche.usb.UsbConnection

interface InputHandler {
    suspend fun pass(input: ToucheInput)
}

@Suppress("Unused")
/**
 * leaving it here for ease of future [ToucheInput] debug inspection
 */
class DummyInputHandler : InputHandler {
    private val TAG = "DummyInputHandler"
    override suspend fun pass(input: ToucheInput) {
        when (input) {
            is ToucheInput.Finger -> {
                Log.d(TAG, "${input.toPointerType()}, coords: ${input.offset}")
            }
            is ToucheInput.Stylus -> {
                Log.d(
                    TAG,
                    "${input.toPointerType()}, coords: ${input.offset}, " +
                            "pressed: ${input.pressed}, pressure: ${input.pressure}"
                )
            }
        }
    }
}

class UsbInputHandler(private val connection: UsbConnection) : InputHandler {
    override suspend fun pass(input: ToucheInput) {
        // TODO: work on a better compression algo
        val data = when (input) {
            is ToucheInput.Finger -> "fingering X:${input.offset.x} Y:${input.offset.y}"
            is ToucheInput.Stylus -> "stylus " +
                    (if (input.pressed) "pressing; pressure: ${input.pressure}"
                    else "hovering like a fucking Harry Potter on his broom") +
                    " X:${input.offset.x} Y:${input.offset.y}"
        }
        connection.write(data.toByteArray())
    }
}