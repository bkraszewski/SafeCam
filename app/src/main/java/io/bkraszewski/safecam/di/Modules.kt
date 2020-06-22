package io.bkraszewski.safecam.di

import android.content.Context
import io.bkraszewski.safecam.feature.StringProvider
import io.bkraszewski.safecam.feature.StringProviderImpl
import io.bkraszewski.safecam.feature.camera.CameraViewModel
import io.bkraszewski.safecam.feature.crypto.SecureImageUseCase
import io.bkraszewski.safecam.feature.crypto.SecureImageUseCaseImpl
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

    fun provideSecureImageUseCase(context: Context): SecureImageUseCase {
        return SecureImageUseCaseImpl(context)
    }

    single<SecureStorage> {
        SecureStorageImpl(androidContext())
    }

    single {
        provideSecureImageUseCase(androidContext())
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
        CameraViewModel(get<SecureImageUseCase>())
    }

}
