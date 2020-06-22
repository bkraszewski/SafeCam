package io.bkraszewski.safecam.feature.crypto

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import io.bkraszewski.safecam.storage.SecureStorage
import java.io.File
import java.io.FileInputStream

interface SecureImageUseCase {
    fun secureImage(inputImagePath: String): String
}

class SecureImageUseCaseImpl(
    private val context: Context,
    private val photoStorageLocationHolder: PhotoStorageLocationHolder,
    private val secureStorage: SecureStorage

) : SecureImageUseCase {
    override fun secureImage(inputImagePath: String): String {
        val keyGenParameterSpec =
        secureStorage.getKeySpec()

        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        val input = File(inputImagePath)
        val output = File(photoStorageLocationHolder.getStoragePath(), input.name)

        try {

            val encryptedFile = EncryptedFile.Builder(output,
                context,
                masterKeyAlias,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()

            val fis = FileInputStream(input)

            val fos = encryptedFile.openFileOutput()
            fis.copyTo(fos)


        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            input.delete()
        }

        return output.absolutePath
    }
}
