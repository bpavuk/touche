package dev.bpavuk.touche.data.persistence

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class OnboardingRepository(private val dataStore: DataStore<Preferences>) {
    fun getOnboardingCompleted(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[PREFKEY_ONBOARDING_COMPLETED] ?: false
        }
    }

    fun getOnboardingCompletedBlocking(): Boolean {
        return runBlocking { getOnboardingCompleted().first() }
    }

    suspend fun setOnboardingCompleted(onboardingCompleted: Boolean) {
        dataStore.updateData { preferences ->
            preferences.toMutablePreferences().also {
                it[PREFKEY_ONBOARDING_COMPLETED] = onboardingCompleted
            }
        }
    }
}
