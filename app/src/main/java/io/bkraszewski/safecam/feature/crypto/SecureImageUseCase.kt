package io.bkraszewski.safecam.feature.crypto

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import io.bkraszewski.safecam.R
import java.io.File

interface SecureImageUseCase {
    fun secureImage(inputImagePath: String): String
}

class SecureImageUseCaseImpl(
    private val context: Context,
    private val photoStorageLocationHolder: PhotoStorageLocationHolder

) : SecureImageUseCase {
    override fun secureImage(inputImagePath: String): String {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        val input = File(inputImagePath)
        val output = File(photoStorageLocationHolder.getStoragePath(), input.name)

        try {

            val encryptedFile = EncryptedFile.Builder(output,
                context,
                masterKeyAlias,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()

            val bytes: ByteArray = input.readBytes()

            encryptedFile.openFileOutput().bufferedWriter().use {
                for (byte in bytes) {
                    it.write(byte.toInt())
                }
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            input.delete()
        }

        return output.absolutePath
    }
}
