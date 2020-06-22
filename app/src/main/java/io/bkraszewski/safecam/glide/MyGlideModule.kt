package io.bkraszewski.safecam.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import io.bkraszewski.safecam.feature.browser.SecureFile
import org.koin.core.context.GlobalContext
import java.nio.ByteBuffer

@GlideModule
class MyAppGlideModule : AppGlideModule() {

    private val factory: EncryptedModelLoaderFactory
        by lazy { GlobalContext.get().koin.get<EncryptedModelLoaderFactory>() }


    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.prepend(SecureFile::class.java, ByteBuffer::class.java, factory)
    }
}


