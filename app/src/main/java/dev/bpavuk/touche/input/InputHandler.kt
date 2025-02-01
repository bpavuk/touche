package dev.bpavuk.touche.input

import dev.bpavuk.touche.input.types.ToucheInput
import dev.bpavuk.touche.usb.UsbConnection

interface InputHandler {
    suspend fun pass(input: List<ToucheInput>)
}

class UsbInputHandler(private val connection: UsbConnection) : InputHandler {
    private fun List<ToucheInput>.compress(): ByteArray = (
            "${this.first().screenSize.x}\t${this.first().screenSize.y}\n"
                    + joinToString("\n") {
                when (it) {
                    is ToucheInput.Finger -> buildString {
                            append("F\t")
                            append("${it.offset.x}\t")
                            append("${it.offset.y}\t")
                            append(if (it.pressed) "1\t" else "0\t")
                            append(it.id.value)
                        }

                    is ToucheInput.Stylus -> buildString {
                            append("S\t")
                            append("${it.offset.x}\t")
                            append("${it.offset.y}\t")
                            append(if (it.pressed) "1\t${it.pressure}" else "0")
                        }
                }
            }
            ).toByteArray()

    override suspend fun pass(input: List<ToucheInput>) {
        val data = input.compress()
        connection.write(data)
    }
}