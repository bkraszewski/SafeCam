package io.bkraszewski.safecam.extensions

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT){
        Toast.makeText(this.requireContext(), text, duration).apply { show() }
}

fun Fragment.toast(text: Int, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(this.requireContext(), text, duration).apply { show() }
}
