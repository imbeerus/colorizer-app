package com.lndmflngs.colorizer

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingAdapters {

  @JvmStatic
  @BindingAdapter("imageUrl")
  fun ImageView.setImageUrl(url: String?) {
    Glide.with(context).load(url).fitCenter().into(this)
  }

}