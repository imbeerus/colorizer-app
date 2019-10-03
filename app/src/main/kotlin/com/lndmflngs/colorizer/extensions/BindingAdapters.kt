package com.lndmflngs.colorizer.extensions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.request.RequestOptions
import com.lndmflngs.colorizer.glide.GlideApp

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(url: String?) {
    val resizeOptions = RequestOptions().override(width, height)
    val glide = GlideApp.with(context)
        .load(url)
        .fitCenter()
    if (width != 0 && height != 0) {
        glide.apply(resizeOptions)
    }
    glide.into(this)
}