package io.bkraszewski.safecam.glide

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoader.LoadData
import com.bumptech.glide.signature.ObjectKey
import io.bkraszewski.safecam.feature.browser.SecureFile
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer


class EncryptedLoader(
    private val imageDecryptor: ImageDecryptor
) : ModelLoader<SecureFile, ByteBuffer> {

    override fun buildLoadData(model: SecureFile, width: Int, height: Int, options: Options): LoadData<ByteBuffer>? {
        return LoadData(ObjectKey(model), EncryptedDataFetcher(imageDecryptor, model.file.absolutePath))
    }

    override fun handles(model: SecureFile): Boolean {
        return model.file.exists()
    }
}

class EncryptedDataFetcher(
    private val imageDecryptor: ImageDecryptor,
    private val fileUrl: String
) : DataFetcher<ByteBuffer> {


    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in ByteBuffer>) {

        val data: ByteArray = imageDecryptor.decrypt(fileUrl)
        val byteBuffer = ByteBuffer.wrap(data)
        callback.onDataReady(byteBuffer)
    }

    override fun cleanup() {}
    override fun cancel() {}
    override fun getDataClass(): Class<ByteBuffer> {
        return ByteBuffer::class.java
    }

    override fun getDataSource(): DataSource {
        return DataSource.LOCAL
    }
}

interface ImageDecryptor {
    fun decrypt(imageUrl: String): ByteArray
}

class ImageDecryptorImpl(
    private val context: Context
) : ImageDecryptor {
    override fun decrypt(imageUrl: String): ByteArray {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

        val encryptedFile = EncryptedFile.Builder(
            File(imageUrl),
            context,
            masterKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        val fis = encryptedFile.openFileInput()
        val bos = ByteArrayOutputStream()
        val buf = ByteArray(1024)

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
