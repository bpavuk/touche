package dev.bpavuk.touche.input

import dev.bpavuk.touche.input.types.ToucheInput
import dev.bpavuk.touche.usb.UsbConnection

interface InputHandler {
    suspend fun pass(input: List<ToucheInput>)
}

class UsbInputHandler(private val connection: UsbConnection) : InputHandler {
    private fun List<ToucheInput>.compress(): ByteArray = joinToString("\n") {
        when (it) {
            is ToucheInput.Finger -> "F\t${it.offset.x}\t${it.offset.y}"
            is ToucheInput.Stylus -> ("S\t${it.offset.x}\t${it.offset.y}\t"
                    + if (it.pressed) "1\t${it.pressure}" else "0")
        }
    }.toByteArray()

    override suspend fun pass(input: List<ToucheInput>) {
        val data = input.compress()
        connection.write(data)
    }
}