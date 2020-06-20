package io.bkraszewski.safecam

import android.app.Application
import io.bkraszewski.safecam.di.applicationModule
import io.bkraszewski.safecam.di.cryptoModule
import io.bkraszewski.safecam.di.viewModelModule
import io.bkraszewski.safecam.storage.SecureStorage
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.Koin
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.unloadKoinModules
import timber.log.Timber

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initLogger()
        initKoin()
        initDefaultPassword()
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
                    cryptoModule,
                    viewModelModule
                )
            )
        }
    }

    private fun initDefaultPassword() {

        val secureStorage = get<SecureStorage>()
        if (secureStorage.getPassword().isEmpty()) {
            secureStorage.setPassword("1111")
        }
    }
}
