package io.bkraszewski.safecam.glide

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoader.LoadData
import com.bumptech.glide.signature.ObjectKey
import io.bkraszewski.safecam.feature.browser.SecureFile
import io.bkraszewski.safecam.feature.crypto.ImageDecryptor
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


