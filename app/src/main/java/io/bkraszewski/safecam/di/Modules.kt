package io.bkraszewski.safecam.di

import io.bkraszewski.safecam.feature.StringProvider
import io.bkraszewski.safecam.feature.StringProviderImpl
import io.bkraszewski.safecam.feature.camera.CameraViewModel
import io.bkraszewski.safecam.feature.crypto.UserAuthenticator
import io.bkraszewski.safecam.feature.crypto.UserAuthenticatorImpl
import io.bkraszewski.safecam.feature.login.LoginViewModel
import io.bkraszewski.safecam.storage.SecureStorage
import io.bkraszewski.safecam.storage.SecureStorageImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module(override = true) {
    factory<StringProvider> { StringProviderImpl(androidContext()) }
}

val cryptoModule = module(override = true) {

    fun provideUserAuthenticator(secureStorage: SecureStorage): UserAuthenticator {
        return UserAuthenticatorImpl(secureStorage)
    }

    single<SecureStorage> {
        SecureStorageImpl(androidContext())
    }

    single {
        provideUserAuthenticator(get())
    }
}

val viewModelModule = module(override = true) {
    viewModel {
        LoginViewModel(
            get<UserAuthenticator>(),
            get<StringProvider>())
    }

    viewModel {
        CameraViewModel()
    }
}
