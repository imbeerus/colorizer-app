package com.lndmflngs.colorizer.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import android.view.Surface
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

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

fun Activity.hideSystemUI() {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    window.decorView.systemUiVisibility = (
        View.SYSTEM_UI_FLAG_IMMERSIVE
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN)
  } else {
    window.decorView.systemUiVisibility = (
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN)
  }
}

fun Activity.showSystemUI() {
  window.decorView.systemUiVisibility = (
      View.SYSTEM_UI_FLAG_LAYOUT_STABLE
      or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
      or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
}

fun Activity.hideKeyboard() {
  val view = this.currentFocus
  if (view != null) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
  }
  // clear focus to avoid showing keyboard again
  view?.clearFocus()
}