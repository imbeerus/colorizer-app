package com.lndmflngs.colorizer.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.toast(str: String, length: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, str, length).show()

fun Context.toast(@StringRes strResId: Int, length: Int = Toast.LENGTH_SHORT) =
    toast(getString(strResId), length)