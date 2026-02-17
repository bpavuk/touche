package dev.bpavuk.touche.data.persistence

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

val PREFKEY_SCREENSAVER_ENABLED = booleanPreferencesKey("screensaver_enabled")
val PREFKEY_SCREENSAVER_ID = stringPreferencesKey("screensaver_id")

val PREFKEY_TOUCHPAD_ENABLED = booleanPreferencesKey("touchpad_enabled")

val PREFKEY_STYLUS_ENABLED = booleanPreferencesKey("stylus_enabled")

val PREFKEY_ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
