package dev.bpavuk.touche.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface ToucheInput {
    @Serializable
    @SerialName("ToucheInput.Stylus")
    data class Stylus(
        val x: Int,
        val y: Int,
        val pressed: Boolean,
        val pressure: Float,
    ) : ToucheInput

    @Serializable
    @SerialName("ToucheInput.Finger")
    data class Finger(
        val x: Int,
        val y: Int,
        val pressed: Boolean,
        val touchId: Long
    ) : ToucheInput

    @Serializable
    @SerialName("ToucheInput.Screen")
    data class Screen(
        val x: Int,
        val y: Int
    ) : ToucheInput


    @Serializable
    @SerialName("ToucheInput.Action")
    enum class Action : ToucheInput {
        @SerialName("ToucheInput.Action.Init")
        Init
    }
}