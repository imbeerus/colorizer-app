package com.lndmflngs.colorizer.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.util.Log
import android.view.Surface
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import com.lndmflngs.colorizer.R

private var prevOrientation = -10

private const val TAG = "ActivityExt"

fun Activity.lockOrientation() {
  if (prevOrientation != -10) {
    return
  }
  try {
    prevOrientation = requestedOrientation
    val manager = getSystemService(Activity.WINDOW_SERVICE) as WindowManager
    if (manager.defaultDisplay != null) {
      val rotation = manager.defaultDisplay.rotation
      val orientation = resources.configuration.orientation

      when (rotation) {
        Surface.ROTATION_270 -> requestedOrientation =
          if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
          } else {
            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
          }
        Surface.ROTATION_90 -> requestedOrientation =
          if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
          } else {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
          }
        Surface.ROTATION_0 -> requestedOrientation =
          if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
          } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
          }
        else -> requestedOrientation =
          if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
          } else {
            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
          }
      }
    }
  } catch (e: Exception) {
    Log.e(TAG, e.message)
  }
}

@SuppressLint("WrongConstant")
fun Activity.unlockOrientation() {
  try {
    if (prevOrientation != -10) {
      requestedOrientation = prevOrientation
      prevOrientation = -10
    }
  } catch (e: Exception) {
    Log.e(TAG, e.message)
  }
}