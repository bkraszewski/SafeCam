package io.bkraszewski.safecam.feature.crypto

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import io.bkraszewski.safecam.storage.SecureStorage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

interface ImageDecryptor {
    fun decrypt(imageUrl: String): ByteArray
}

class ImageDecryptorImpl(
    private val context: Context,
    private val secureStorage: SecureStorage
) : ImageDecryptor {
    override fun decrypt(imageUrl: String): ByteArray {
        val keyGenParameterSpec =
            secureStorage.getKeySpec()
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

        val encryptedFile = EncryptedFile.Builder(
            File(imageUrl),
            context,
            masterKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        //val fis = FileInputStream(File(imageUrl))

        val fis = encryptedFile.openFileInput()
        val bos = ByteArrayOutputStream()
        val buf = ByteArray(DEFAULT_BUFFER_SIZE)

        try {
            var readNum: Int
            while (fis.read(buf).also { readNum = it } != -1) {
                bos.write(buf, 0, readNum)

            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return bos.toByteArray()
    }

}
