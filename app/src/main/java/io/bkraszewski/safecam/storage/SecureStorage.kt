package io.bkraszewski.safecam.storage

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys


interface SecureStorage{
    fun setPassword(password: String)
    fun getPassword(): String
    fun getKeySpec(): KeyGenParameterSpec
}

class SecureStorageImpl(
    context: Context
): SecureStorage{

    private val sharedPreferences: SharedPreferences

    init {
        val masterKeyAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        sharedPreferences = EncryptedSharedPreferences.create(
            "secret_shared_prefs",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
    override fun setPassword(password: String) {
        sharedPreferences.edit().putString(PASSWORD, password).apply()
    }

    override fun getPassword(): String = sharedPreferences.getString(PASSWORD, "")!!

    override fun getKeySpec(): KeyGenParameterSpec {
        return KeyGenParameterSpec.Builder(
            getPassword(),
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()
    }

    companion object{
        const val PASSWORD = "password"
    }
}
