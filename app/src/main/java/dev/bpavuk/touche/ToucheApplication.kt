package dev.bpavuk.touche

import android.app.Application
import dev.bpavuk.touche.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ToucheApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@ToucheApplication)
            modules(appModule)
        }
    }
}
