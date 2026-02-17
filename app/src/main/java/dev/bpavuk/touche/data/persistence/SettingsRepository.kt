package dev.bpavuk.touche.data.persistence

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.bpavuk.touche.ui.screens.settings.Screensavers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(
    private val dataStore: DataStore<Preferences>
) {
    // Screensaver settings

    fun getScreensaverEnabled(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PREFKEY_SCREENSAVER_ENABLED] ?: true
    }

    suspend fun setScreensaverEnabled(screensaverEnabled: Boolean) {
        dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[PREFKEY_SCREENSAVER_ENABLED] = screensaverEnabled
            }
        }
    }

    fun getScreensaverId(): Flow<String> = dataStore.data.map { preferences ->
        preferences[PREFKEY_SCREENSAVER_ID] ?: Screensavers.cloudy.id
    }

    suspend fun setScreensaverId(screensaverId: String) {
        dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[PREFKEY_SCREENSAVER_ID] = screensaverId
            }
        }
    }

    // Touchpad settings

    fun getTouchpadEnabled(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PREFKEY_TOUCHPAD_ENABLED] ?: true
    }

    suspend fun setTouchpadEnabled(touchpadEnabled: Boolean) {
        dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[PREFKEY_TOUCHPAD_ENABLED] = touchpadEnabled
            }
        }
    }

    // Stylus settings

    fun getStylusEnabled(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PREFKEY_STYLUS_ENABLED] ?: true
    }

    suspend fun setStylusEnabled(stylusEnabled: Boolean) {
        dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[PREFKEY_STYLUS_ENABLED] = stylusEnabled
            }
        }
    }
}