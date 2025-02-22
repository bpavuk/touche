package dev.bpavuk.touche.input

import dev.bpavuk.touche.input.types.ToucheInput
import dev.bpavuk.touche.usb.UsbConnection
import kotlin.math.min

interface InputHandler {
    suspend fun pass(input: List<ToucheInput>)
}

class UsbInputHandler(private val connection: UsbConnection) : InputHandler {
    private fun List<ToucheInput>.compress(): ByteArray = (
            joinToString("\n") {
                when (it) {
                    is ToucheInput.Finger -> {
                        val scale = min(
                            4096 / it.screenSize.x.toFloat(),
                            4096 / it.screenSize.y.toFloat()
                        )
                        buildString {
                            append("F\t")
                            append("${it.offset.x * scale}\t")
                            append("${it.offset.y * scale}\t")
                            append(if (it.pressed) "1\t" else "0\t")
                            append(it.id.value)
                        }
                    }

                    is ToucheInput.Stylus -> buildString {
                        append("S\t")
                        append("${it.offset.x * 4096 / it.screenSize.x}\t")
                        append("${it.offset.y * 4096 / it.screenSize.y}\t")
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