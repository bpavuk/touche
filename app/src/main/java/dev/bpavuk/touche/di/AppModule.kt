package dev.bpavuk.touche.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dev.bpavuk.touche.data.ToucheRepository
import dev.bpavuk.touche.data.ToucheRepositoryImpl
import dev.bpavuk.touche.data.UsbConnection
import dev.bpavuk.touche.data.persistence.OnboardingRepository
import dev.bpavuk.touche.data.persistence.SettingsRepository
import dev.bpavuk.touche.logic.input.InputViewModelImpl
import dev.bpavuk.touche.logic.settings.ScreensaverSettingsViewModelImpl
import dev.bpavuk.touche.logic.settings.StylusSettingsViewModelImpl
import dev.bpavuk.touche.logic.settings.TouchpadSettingsViewModelImpl
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

private const val SETTINGS_DATASTORE_NAME = "settings"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_DATASTORE_NAME)

@OptIn(ExperimentalSerializationApi::class)
val appModule = module {
    single { androidContext().dataStore }
    single { SettingsRepository(get()) }
    single { OnboardingRepository(get()) }
    viewModelOf(::TouchpadSettingsViewModelImpl)
    viewModelOf(::ScreensaverSettingsViewModelImpl)
    viewModelOf(::StylusSettingsViewModelImpl)
    factory<ToucheRepository> { (conn: UsbConnection) -> ToucheRepositoryImpl(usb = conn) }
    factory<InputViewModelImpl> { (conn: UsbConnection) -> InputViewModelImpl(
        client = get { parametersOf(conn) }, settings = get())
    }
}
