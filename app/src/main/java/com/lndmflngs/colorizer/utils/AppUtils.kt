package com.lndmflngs.colorizer.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import android.view.Surface
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.ui.dialogs.AboutDialog

object AppUtils {

  private val TAG = "AppUtils"
  private var prevOrientation = -10

  const val REQUEST_TAKE_IMAGE = 0

  fun startAboutApp(activity: Activity) = with(activity) {
    val fragmentManager = (this as AppCompatActivity).supportFragmentManager
    AboutDialog().show(fragmentManager, AboutDialog.TAG)
  }

  fun startPickImage(activity: Activity) = with(activity) {
    val pickPhoto = Intent(Intent.ACTION_GET_CONTENT).apply {
      addCategory(Intent.CATEGORY_OPENABLE)
      type = "image/*"
    }
    val str = getString(R.string.title_select_picture)
    // android.app.SuperNotCalledException: Activity {android/com.android.internal.app.ChooserActivity} did not call through to super.onStop()
    // just an emulator issue (http://qaru.site/questions/12400777/sharecompat-intentbuilder-crashing-every-time-on-android-4)
    startActivityForResult(Intent.createChooser(pickPhoto, str), REQUEST_TAKE_IMAGE)
  }

  fun openBrowser(activity: Activity, link: String) = with(activity) {
    val url = if (!link.startsWith("http://") && !link.startsWith("https://")) {
      "http://$link"
    } else {
      link
    }
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(browserIntent)
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

        when (rotation) {
          Surface.ROTATION_270 -> activity.requestedOrientation =
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
              ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } else {
              ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
            }
          Surface.ROTATION_90 -> activity.requestedOrientation =
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
              ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
            } else {
              ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
          Surface.ROTATION_0 -> activity.requestedOrientation =
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
              ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            } else {
              ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
          else -> activity.requestedOrientation =
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