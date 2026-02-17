package dev.bpavuk.touche.logic.input

import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bpavuk.touche.data.ToucheRepository
import dev.bpavuk.touche.data.model.ToucheInput
import dev.bpavuk.touche.data.persistence.SettingsRepository
import dev.bpavuk.touche.ui.screens.settings.Screensaver
import dev.bpavuk.touche.ui.screens.settings.Screensavers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

interface InputViewModel {
    fun sendPointerEvent(
        inputChange: List<PointerInputChange>,
        stylusEnabled: Boolean,
        touchpadEnabled: Boolean
    )
    fun sendScreenEvent(screenSize: IntSize)
    fun getStylusEnabled(): Flow<Boolean>
    fun getTouchpadEnabled(): Flow<Boolean>
    fun getScreensaverEnabled(): Flow<Boolean>
    fun getScreensaver(): Flow<Screensaver?>
}

class InputViewModelImpl(
    private val client: ToucheRepository,
    private val settings: SettingsRepository,
) : InputViewModel, ViewModel() {
    override fun sendPointerEvent(
        inputChange: List<PointerInputChange>,
        stylusEnabled: Boolean,
        touchpadEnabled: Boolean
    ) {
        viewModelScope.launch {
            client.sendEvents(inputChange.mapNotNull {
                val input = it.toToucheInput(
                    stylusEnabled,
                    touchpadEnabled
                )
                if (input != null) it.consume()
                input
            })
        }
    }

    override fun sendScreenEvent(screenSize: IntSize) {
        viewModelScope.launch {
            client.sendEvent(ToucheInput.Action.Init)
            client.sendEvent(screenSize.toToucheInput())
        }
    }

    override fun getStylusEnabled(): Flow<Boolean> {
        return settings.getStylusEnabled()
    }

    override fun getTouchpadEnabled(): Flow<Boolean> {
        return settings.getTouchpadEnabled()
    }

    override fun getScreensaverEnabled(): Flow<Boolean> {
        return settings.getScreensaverEnabled()
    }

    override fun getScreensaver(): Flow<Screensaver?> {
        return settings.getScreensaverId().map { id ->
            Screensavers.all().find { it.id == id }
        }
    }

    private fun IntSize.toToucheInput(): ToucheInput =
        toSize().run { ToucheInput.Screen((width).toInt(), (height).toInt()) }

    private fun PointerInputChange.toToucheInput(
        stylusEnabled: Boolean,
        touchpadEnabled: Boolean
    ): ToucheInput? {
        return when (this.type) {
            PointerType.Stylus if stylusEnabled -> ToucheInput.Stylus(
                x = position.x.toInt(),
                y = position.y.toInt(),
                pressed = pressed,
                pressure = pressure
            )

            PointerType.Touch if touchpadEnabled -> {
                ToucheInput.Finger(
                    x = position.x.toInt(),
                    y = position.y.toInt(),
                    pressed = pressed,
                    touchId = id.value
                )
            }

            else -> null
        }
    }
}
