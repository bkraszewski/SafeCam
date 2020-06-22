package io.bkraszewski.safecam.feature.browser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.bkraszewski.safecam.BR
import io.bkraszewski.safecam.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.ItemBinding


class BrowserViewModel(
    getImagesUseCase: GetImagesUseCase
) : ViewModel() {
    val items = MutableLiveData<List<SecureFile>>().apply {
        value = listOf()
    }
    val itemBinding = ItemBinding.of<SecureFile>(BR.item, R.layout.item_image)

    init {
        viewModelScope.launch(Dispatchers.IO)  {
            val images = getImagesUseCase.getImages().map {
                SecureFile(it)
            }
            items.value = images
        }
    }
}
