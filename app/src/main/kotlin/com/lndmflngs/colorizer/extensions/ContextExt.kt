package com.lndmflngs.colorizer.extensions

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.StringRes


fun Context.toast(str: String, length: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, str, length).show()

fun Context.toast(@StringRes strResId: Int, length: Int = Toast.LENGTH_SHORT) =
    toast(getString(strResId), length)

val Context.displayMetrics: DisplayMetrics
    get() {
        val displayMetrics = DisplayMetrics()
        (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(
            displayMetrics
        )
        return displayMetrics
    }