package com.lndmflngs.colorizer.extensions

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.ui.main.MainActivity

fun AppCompatActivity.startPickImage() {
  val pickPhoto = Intent(Intent.ACTION_GET_CONTENT).apply {
    addCategory(Intent.CATEGORY_OPENABLE)
    type = "image/*"
  }
  val str = getString(R.string.title_select_picture)
  // android.app.SuperNotCalledException: Activity {android/com.android.internal.app.ChooserActivity} did not call through to super.onStop()
  // just an emulator issue (http://qaru.site/questions/12400777/sharecompat-intentbuilder-crashing-every-time-on-android-4)
  startActivityForResult(Intent.createChooser(pickPhoto, str), MainActivity.REQUEST_TAKE_IMAGE)
}

fun AppCompatActivity.openBrowser(link: String) {
  val url = if (!link.startsWith("http://") && !link.startsWith("https://")) {
    "http://$link"
  } else {
    link
  }
  val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
  startActivity(browserIntent)
}

inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
  beginTransaction().apply {
    action()
  }.commit()
}