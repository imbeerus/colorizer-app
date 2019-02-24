package com.lndmflngs.colorizer.extensions

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat

fun Context.color(res: Int): Int = ContextCompat.getColor(this, res)

fun Context.toast(str: String, duration: Int = Toast.LENGTH_SHORT) =
  Toast.makeText(this, str, duration).show()

fun Context.toast(strId: Int, duration: Int = Toast.LENGTH_SHORT) =
  Toast.makeText(this, getString(strId), duration).show()