package com.lndmflngs.colorizer.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.Surface
import android.view.WindowManager
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.R.color
import com.lndmflngs.colorizer.extensions.color
import com.lndmflngs.colorizer.extensions.getBitmapUri
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder
import com.mikepenz.aboutlibraries.util.Colors
import kotlinx.android.synthetic.main.include_result.resultImageView

object AppUtils {

  private val TAG = "AppUtils"
  private var prevOrientation = -10

  const val REQUEST_TAKE_IMAGE = 0

  fun startAboutApp(activity: Activity) = with(activity) {
    val styleColors = Colors(color(color.primaryColor), color(color.primaryDarkColor))
    LibsBuilder()
      .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
      .withActivityColor(styleColors)
      .withAboutIconShown(true)
      .withAboutVersionShown(true)
      .withAboutDescription(getString(R.string.msg_description))
      .start(this)
  }

  fun startPickImage(activity: Activity) = with(activity) {
    val pickPhoto = Intent(Intent.ACTION_GET_CONTENT).apply {
      addCategory(Intent.CATEGORY_OPENABLE)
      type = "image/*"
    }
    val str = getString(R.string.title_select_picture)
    startActivityForResult(Intent.createChooser(pickPhoto, str), REQUEST_TAKE_IMAGE)
  }

  fun shareResultImgFile(activity: Activity, uri: Uri) = with(activity) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
      putExtra(Intent.EXTRA_STREAM, uri)
      type = "image/*"
    }
    startActivity(Intent.createChooser(shareIntent, getString(R.string.title_share_via)))
  }

  fun lockOrientation(activity: Activity?) {
    if (activity == null || prevOrientation != -10) {
      return
    }
    try {
      prevOrientation = activity.requestedOrientation
      val manager = activity.getSystemService(Activity.WINDOW_SERVICE) as WindowManager
      if (manager.defaultDisplay != null) {
        val rotation = manager.defaultDisplay.rotation
        val orientation = activity.resources.configuration.orientation

        if (rotation == Surface.ROTATION_270) {
          activity.requestedOrientation = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
          } else {
            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
          }
        } else if (rotation == Surface.ROTATION_90) {
          activity.requestedOrientation = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
          } else {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
          }
        } else if (rotation == Surface.ROTATION_0) {
          activity.requestedOrientation = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
          } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
          }
        } else {
          activity.requestedOrientation = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
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
  fun unlockOrientation(activity: Activity?) {
    if (activity == null) {
      return
    }
    try {
      if (prevOrientation != -10) {
        activity.requestedOrientation = prevOrientation
        prevOrientation = -10
      }
    } catch (e: Exception) {
      Log.e(TAG, e.message)
    }
  }
}