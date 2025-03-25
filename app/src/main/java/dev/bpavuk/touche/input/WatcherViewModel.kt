package dev.bpavuk.touche.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bpavuk.touche.input.types.ToucheScreenSize
import dev.bpavuk.touche.input.types.ToucheInput
import dev.bpavuk.touche.usb.UsbConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WatcherViewModel(private val connection: UsbConnection) : ViewModel() {
    private var state: WatcherState = WatcherState.initial

    fun watch() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                val opcode = connection.read()

                while (state == WatcherState.initial || state.encode(opcode) == null) {
                    delay(1000 / 60) // polling state at 60Hz
                }

                state.encode(opcode)?.let { connection.write(it) }
            }
        }
    }

    fun pass(input: List<ToucheInput>) {
        state = state.copy(input = input)
    }

    fun setScreenState(screen: ToucheScreenSize) {
        state = state.copy(screen = screen)
    }
}

class InvalidOpcodeException(opcode: Int) : IllegalStateException(
    "Invalid opcode received: $opcode"
)

private data class WatcherState(
    val input: List<ToucheInput>, val screen: ToucheScreenSize
) {
    companion object {
        val initial = WatcherState(emptyList(), ToucheScreenSize(0, 0))
    }

    fun encode(opcode: Int): ByteArray? {
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

private fun List<ToucheInput>.encode(): ByteArray? = map { it.encode() }.reduceOrNull { acc, toucheInput ->
    acc + "\n".encodeToByteArray() + toucheInput
}