package io.bkraszewski.safecam.feature.crypto

import android.content.Context
import io.bkraszewski.safecam.R
import java.io.File

interface PhotoStorageLocationHolder {
    fun getStoragePath(): File
}

class PhotoStorageLocationHolderImpl(
    private val context: Context
) : PhotoStorageLocationHolder{
    override fun getStoragePath(): File {
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, context.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else context.filesDir
    }

}
