package io.bkraszewski.safecam

import android.app.Application
import io.bkraszewski.safecam.di.applicationModule
import io.bkraszewski.safecam.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initLogger()
        initKoin()
    }

    private fun initLogger() {
        Timber.plant(Timber.DebugTree())
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@BaseApplication)
            modules(
                listOf(
                    applicationModule,
                     viewModelModule
                )
            )
        }
    }
}
