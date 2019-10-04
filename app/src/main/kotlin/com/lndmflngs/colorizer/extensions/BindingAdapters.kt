package com.lndmflngs.colorizer.extensions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.request.RequestOptions
import com.lndmflngs.colorizer.glide.GlideApp

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(url: String?) {
    url?.let {
        val glide = GlideApp.with(context)
            .load(url)
            .fitCenter()
            .apply(RequestOptions().override(width, height))
        glide.into(this)
    }
}