package com.lndmflngs.colorizer.extensions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.lndmflngs.colorizer.di.module.GlideApp

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(url: String?) {
  GlideApp.with(context).load(url).fitCenter().into(this)
}