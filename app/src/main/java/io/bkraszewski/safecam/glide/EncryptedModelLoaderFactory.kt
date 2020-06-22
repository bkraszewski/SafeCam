package io.bkraszewski.safecam.glide

import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import io.bkraszewski.safecam.feature.browser.SecureFile
import io.bkraszewski.safecam.feature.crypto.ImageDecryptor
import java.nio.ByteBuffer

class EncryptedModelLoaderFactory(
    private val decryptor: ImageDecryptor
) : ModelLoaderFactory<SecureFile, ByteBuffer> {
    override fun build(unused: MultiModelLoaderFactory): ModelLoader<SecureFile, ByteBuffer> {
        return EncryptedLoader(decryptor)
    }

    override fun teardown() {
        // Do nothing.
    }
}
