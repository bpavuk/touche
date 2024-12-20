package dev.bpavuk.touche.input

import android.util.Log
import dev.bpavuk.touche.input.types.ToucheInput
import dev.bpavuk.touche.input.types.toPointerType

interface InputHandler {
    fun pass(input: ToucheInput)
}

class DummyInputHandler : InputHandler {
    private val TAG = "DummyInputHandler"
    override fun pass(input: ToucheInput) {
        when (input) {
            is ToucheInput.Finger -> {
                Log.d(TAG, "${input.toPointerType()}, coords: ${input.offset}")
            }
            is ToucheInput.Stylus -> {
                Log.d(
                    TAG,
                    "${input.toPointerType()}, coords: ${input.offset}, pressed: ${input.pressed}"
                )
            }
        }
    }
}
