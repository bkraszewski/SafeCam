package io.bkraszewski.safecam.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import io.bkraszewski.safecam.feature.browser.SecureFile

object EncryptingAdapter{

    @JvmStatic
    @BindingAdapter("encryptedImagePath")
    fun loadEncrypted(imageView: ImageView, secureFile: SecureFile){
        Glide.with(imageView.context)
            .load(secureFile)
            .into(imageView);
    }
}
