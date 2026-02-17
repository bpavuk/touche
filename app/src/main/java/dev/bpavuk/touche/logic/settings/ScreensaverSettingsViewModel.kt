package dev.bpavuk.touche.logic.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bpavuk.touche.data.persistence.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

interface ScreensaverSettingsViewModel {
    fun getScreensaverEnabled(): Flow<Boolean>
    fun setScreensaverEnabled(screensaverEnabled: Boolean)
    fun getScreensaverId(): Flow<String>
    fun setScreensaverId(screensaverId: String)
}

class ScreensaverSettingsViewModelImpl(
    private val settingsRepository: SettingsRepository
): ScreensaverSettingsViewModel, ViewModel() {
    override fun getScreensaverEnabled(): Flow<Boolean> {
        return settingsRepository.getScreensaverEnabled()
    }

    override fun setScreensaverEnabled(screensaverEnabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setScreensaverEnabled(screensaverEnabled)
        }
    }

    override fun getScreensaverId(): Flow<String> {
        return settingsRepository.getScreensaverId()
    }

    override fun setScreensaverId(screensaverId: String) {
        viewModelScope.launch {
            settingsRepository.setScreensaverId(screensaverId)
        }
    }
}