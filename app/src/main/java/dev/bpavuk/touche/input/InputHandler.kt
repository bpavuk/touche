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
    private fun ToucheInput.compress(): ByteArray = when (this) {
        is ToucheInput.Finger -> "F${offset.x}\t${offset.y}".toByteArray()
        is ToucheInput.Stylus -> ("S${offset.x}\t${offset.y}\t"
                + if (pressed) "1\t${pressure}" else "0").toByteArray()
    }

    override suspend fun pass(input: ToucheInput) {
        val data = input.compress()
        connection.write(data)
    }
}