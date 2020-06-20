package io.bkraszewski.safecam.bindingadapters

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun visible(view: View, visible: Boolean){
    view.isVisible = visible
}
