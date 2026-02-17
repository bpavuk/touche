package dev.bpavuk.touche.logic.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bpavuk.touche.data.persistence.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

interface TouchpadSettingsViewModel {
    fun getTouchpadEnabled(): Flow<Boolean>
    fun setTouchpadEnabled(touchpadEnabled: Boolean)
}

class TouchpadSettingsViewModelImpl(
    private val settingsRepository: SettingsRepository
): TouchpadSettingsViewModel, ViewModel() {
    override fun getTouchpadEnabled(): Flow<Boolean> {
        return settingsRepository.getTouchpadEnabled()
    }

    override fun setTouchpadEnabled(touchpadEnabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setTouchpadEnabled(touchpadEnabled)
        }
    }
}
