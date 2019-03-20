package com.lndmflngs.colorizer.extensions

import android.content.Context
import android.widget.Toast

fun Context.toast(str: String, length: Int = Toast.LENGTH_SHORT) {
  Toast.makeText(this, str, length).show()
}