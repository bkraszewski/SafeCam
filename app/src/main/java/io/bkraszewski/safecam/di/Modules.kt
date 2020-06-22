package io.bkraszewski.safecam.di

import android.content.Context
import io.bkraszewski.safecam.feature.StringProvider
import io.bkraszewski.safecam.feature.StringProviderImpl
import io.bkraszewski.safecam.feature.browser.BrowserViewModel
import io.bkraszewski.safecam.feature.browser.GetImagesUseCase
import io.bkraszewski.safecam.feature.browser.GetImagesUseCaseImpl
import io.bkraszewski.safecam.feature.camera.CameraViewModel
import io.bkraszewski.safecam.feature.crypto.*
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

    fun providePhotoStorageLocationHolder(context: Context): PhotoStorageLocationHolder {
        return PhotoStorageLocationHolderImpl(context)
    }

    fun provideSecureImageUseCase(
        context: Context,
        photoStorageLocationHolder: PhotoStorageLocationHolder): SecureImageUseCase {
        return SecureImageUseCaseImpl(context, photoStorageLocationHolder)
    }

    fun provideGetImagesUseCase(
        photoStorageLocationHolder: PhotoStorageLocationHolder): GetImagesUseCase {
        return GetImagesUseCaseImpl(photoStorageLocationHolder)
    }

    single<SecureStorage> {
        SecureStorageImpl(androidContext())
    }

    single {
        providePhotoStorageLocationHolder(androidContext())
    }

    single {
        provideSecureImageUseCase(androidContext(), get())
    }

    single {
        provideUserAuthenticator(get())
    }

    single {
        provideGetImagesUseCase(get())
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

    viewModel {
        BrowserViewModel(get())
    }

}
