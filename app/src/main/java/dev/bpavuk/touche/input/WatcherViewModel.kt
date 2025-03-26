package dev.bpavuk.touche.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bpavuk.touche.input.types.ToucheInput
import dev.bpavuk.touche.input.types.ToucheScreenSize
import dev.bpavuk.touche.usb.UsbConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WatcherViewModel(private val connection: UsbConnection) : ViewModel() {
    private var state: WatcherState = WatcherState.initial

    fun watch() {
        viewModelScope.launch(Dispatchers.IO) {
            opcodeLoop@while (true) {
                val opcode = connection.read()

                stateLoop@while (true) {
                    // TODO: rewrite with non-local break once Kotlin 2.2 is out
                    val encodedState = state.encode(opcode) ?: continue@stateLoop

                    delay(5) // poll state at 200Hz
                    connection.write(encodedState)
                    break@stateLoop

                    // Kotlin 2.2 variant
//                    state.encode(opcode)?.let { encodedState ->
//                        connection.write(encodedState)
//                        break@stateLoop;
//                    } ?: delay(1000 / 60)
                }
            }
        }
    }

    fun pass(input: List<ToucheInput>) {
        state = WatcherState(
            input = input,
            screen = state.screen
        )
    }

    fun setScreenState(screen: ToucheScreenSize) {
        state = WatcherState(
            input = state.input,
            screen = screen
        )
    }
}

class InvalidOpcodeException(opcode: Int) : IllegalStateException(
    "Invalid opcode received: $opcode"
)

private data class WatcherState(
    val input: List<ToucheInput>, val screen: ToucheScreenSize
) {
    override fun toString(): String = "discard: $discard; input: $input, screen: $screen"

    private var discard = false

    private constructor(
        input: List<ToucheInput>, screen: ToucheScreenSize, discard: Boolean
    ) : this(input, screen) {
        this.discard = discard
    }

    companion object {
        val initial = WatcherState(emptyList(), ToucheScreenSize(0, 0), true)
    }

    fun encode(opcode: Int): ByteArray? {
        if (discard) return null
        return when (opcode) {
            1 -> {
                input.encode()
            }

            2 -> {
                screen.encode()
            }

            else -> throw InvalidOpcodeException(opcode)
        }
    }
}

private fun List<ToucheInput>.encode(): ByteArray? =
    map { it.encode() }.reduceOrNull { acc, toucheInput ->
        acc + "\n".encodeToByteArray() + toucheInput
    }