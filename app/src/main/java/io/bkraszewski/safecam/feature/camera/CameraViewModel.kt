package io.bkraszewski.safecam.feature.camera

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.bkraszewski.safecam.feature.crypto.SecureImageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CameraViewModel(
    private val secureImageUseCase: SecureImageUseCase
) : ViewModel() {

    val showPermissionsError = MutableLiveData<Boolean>().apply {
        value = false
    }

    val navigateToGallery = MutableLiveData<Any>()

    fun onPermissionsChecked(areAllPermissionsGranted: Boolean) {
        showPermissionsError.value = !areAllPermissionsGranted
    }

    fun onImageCaptured(savedUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            secureImageUseCase.secureImage(savedUri.path!!)
        }
    }

    fun onGalleryRequested() {
        navigateToGallery.value = true
    }

}
