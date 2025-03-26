package dev.bpavuk.touche.input

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bpavuk.touche.input.types.ToucheInput
import dev.bpavuk.touche.input.types.ToucheScreenSize
import dev.bpavuk.touche.usb.UsbConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class WatcherViewModel(private val connection: UsbConnection) : ViewModel() {
    private val stateFlow = MutableStateFlow(WatcherState.initial)
    private val opcodeFlow = flow {
        while (true) emit(Opcode(connection.read()))
    }.flowOn(Dispatchers.IO)

    private val dataFlow = combine(opcodeFlow, stateFlow) { it1, it2 -> it1 to it2 }

    fun watch() {
        viewModelScope.launch {
            dataFlow.collect { (opcode, state) ->
                Log.d("Watcher", "data flow collected")
                if (opcode.consumed || state.consumed || state == WatcherState.initial) {
                    return@collect
                }

                val encodedState = state.encode(opcode) ?: return@collect
                connection.write(encodedState)
                opcode.consume()
            }
        }
    }

    suspend fun pass(input: List<ToucheInput>) {
        stateFlow.emit(
            stateFlow.value.copy(input = input)
        )
    }

    suspend fun setScreenState(screen: ToucheScreenSize) {
        stateFlow.emit(
            stateFlow.value.copy(screen = screen)
        )
    }
}

class InvalidOpcodeException(opcode: Opcode) : IllegalStateException(
    "Invalid opcode received: ${opcode.value}"
)

private data class WatcherState(
    val input: List<ToucheInput>, val screen: ToucheScreenSize
) {
    var consumed = false

    companion object {
        val initial = WatcherState(emptyList(), ToucheScreenSize(0, 0))
    }

    fun encode(opcode: Opcode): ByteArray? {
        consume()
        return when (opcode.value) {
            1 -> {
                input.encode()
            }

            2 -> {
                screen.encode()
            }

            else -> throw InvalidOpcodeException(opcode)
        }
    }

    fun consume() {
        consumed = true
    }
}

private fun List<ToucheInput>.encode(): ByteArray? =
    map { it.encode() }.reduceOrNull { acc, toucheInput ->
        acc + "\n".encodeToByteArray() + toucheInput
    }

data class Opcode(
    val value: Int, var consumed: Boolean = false
) {
    fun consume() {
        consumed = true
    }
}