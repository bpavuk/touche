package dev.bpavuk.touche.connectivity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bpavuk.touche.connectivity.usb.UsbConnection
import dev.bpavuk.touche.types.Opcode
import dev.bpavuk.touche.types.ToucheInput
import dev.bpavuk.touche.types.ToucheScreenSize
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.IOException

class WatcherViewModel(private val connection: UsbConnection) : ViewModel() {
    private val stateFlow = MutableStateFlow(WatcherState.initial)
    private val opcodeFlow = flow {
        try {
            while (currentCoroutineContext().isActive) {
                emit(Opcode(connection.read()))
            }
        } catch (e: IOException) {
            Log.d("Watcher", "I/O exception. Details:\n$e")
        }
    }

    private val dataFlow = combine(opcodeFlow, stateFlow) { it1, it2 -> it1 to it2 }

    fun watch() {
        viewModelScope.launch {
            dataFlow.collect { (opcode, state) ->
                if (state.consumed || state == WatcherState.initial) {
                    return@collect
                }

                val encodedState = state.encode(opcode) ?: return@collect
                connection.write(encodedState)
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
                screen.encode().encodeToByteArray()
            }

            else -> throw InvalidOpcodeException(opcode)
        }
    }

    fun consume() {
        consumed = true
    }

    private fun List<ToucheInput>.encode(): ByteArray? {
        val data1 = map {
            it.encode()
        }
        if (data1.isEmpty()) return null
        val data = data1.joinToString("\n")
        return data.encodeToByteArray()
    }
}

