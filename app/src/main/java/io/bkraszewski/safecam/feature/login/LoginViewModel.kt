package io.bkraszewski.safecam.feature.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.bkraszewski.safecam.R
import io.bkraszewski.safecam.feature.StringProvider
import io.bkraszewski.safecam.feature.crypto.UserAuthenticator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userAuthenticator: UserAuthenticator,
    private val stringProvider: StringProvider
) : ViewModel() {

    val navigateToCamera: MutableLiveData<Any> = MutableLiveData()
    val password = MutableLiveData<String>().apply {
        postValue("")
    }

    val passwordError = MutableLiveData<String>().apply {
        postValue("")
    }

    init {
        password.observeForever {
            passwordError.value = ""
        }
    }

    fun onPasswordSubmitted() {
        viewModelScope.launch {
            if (userAuthenticator.isValid(password.value!!)) {
                navigateToCamera.postValue(true)
            } else {
                //password.value = ""
                passwordError.value = stringProvider.provideString(R.string.login_wrong_password)
            }
        }
    }

}
