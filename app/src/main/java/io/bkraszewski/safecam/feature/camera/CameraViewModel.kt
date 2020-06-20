package io.bkraszewski.safecam.feature.camera

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CameraViewModel: ViewModel(

) {

    val showPermissionsError = MutableLiveData<Boolean>().apply {
        value = false
    }

    fun onPermissionsChecked(areAllPermissionsGranted: Boolean) {
        showPermissionsError.value = !areAllPermissionsGranted
    }

}
