package io.bkraszewski.safecam.feature.crypto

import io.bkraszewski.safecam.storage.SecureStorage

interface UserAuthenticator {
    suspend fun isValid(password: String): Boolean
}

class UserAuthenticatorImpl(
    private val secureStorage: SecureStorage
) : UserAuthenticator {
    override suspend fun isValid(password: String): Boolean {
        return secureStorage.getPassword() == password
    }

}
