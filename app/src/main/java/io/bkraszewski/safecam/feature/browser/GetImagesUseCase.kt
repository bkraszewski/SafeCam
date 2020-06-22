package io.bkraszewski.safecam.feature.browser

import io.bkraszewski.safecam.feature.crypto.PhotoStorageLocationHolder
import java.io.File

interface GetImagesUseCase {
    fun getImages(): List<File>
}

class GetImagesUseCaseImpl(
    private val holder: PhotoStorageLocationHolder
) : GetImagesUseCase {

    override fun getImages(): List<File> {
        return holder.getStoragePath().listFiles()?.toList() ?: listOf()
    }

}
