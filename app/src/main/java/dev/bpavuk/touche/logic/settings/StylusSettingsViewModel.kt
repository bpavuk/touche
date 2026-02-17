package dev.bpavuk.touche.logic.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bpavuk.touche.data.persistence.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

interface StylusSettingsViewModel {
    fun getStylusEnabled(): Flow<Boolean>
    fun setStylusEnabled(stylusEnabled: Boolean)
}

class StylusSettingsViewModelImpl(
    private val settingsRepository: SettingsRepository
): StylusSettingsViewModel, ViewModel() {
    override fun getStylusEnabled(): Flow<Boolean> {
        return settingsRepository.getStylusEnabled()
    }

    override fun setStylusEnabled(stylusEnabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setStylusEnabled(stylusEnabled)
        }
    }
}