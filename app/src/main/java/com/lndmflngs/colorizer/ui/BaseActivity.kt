package com.lndmflngs.colorizer.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.extensions.checkFragmentClass
import com.lndmflngs.colorizer.extensions.getMediaBitmap
import com.lndmflngs.colorizer.extensions.replaceFragment
import com.lndmflngs.colorizer.extensions.setupActionBar
import com.lndmflngs.colorizer.extensions.toByteArray
import com.lndmflngs.colorizer.extensions.toast
import com.lndmflngs.colorizer.ui.fragments.ResultFragment
import com.lndmflngs.colorizer.utils.GlideUtils
import kotlinx.android.synthetic.main.include_toolbar.toolbar

abstract class BaseActivity : AppCompatActivity() {

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    window.setBackgroundDrawableResource(android.R.drawable.screen_background_light)
    setupActionBar(toolbar) {
      setDisplayHomeAsUpEnabled(true)
      setHomeAsUpIndicator(R.drawable.ic_leaves)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
//      R.id.settings -> {
//        true
//      }
//      R.id.feedback -> {
//        true
//      }
      R.id.about_app -> {
        toast("about_app")
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == REQUEST_TAKE_IMAGE && resultCode == Activity.RESULT_OK) {
      val bitmap = getMediaBitmap(data?.data!!)
      handleImage(bitmap)
    }
  }

  protected fun handleIntent(homeScreenStart: () -> Unit) {
    when {
      intent?.action == Intent.ACTION_SEND -> {
        if (intent.type?.startsWith("image/") == true) {
          val uri: Uri = (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)!!
          GlideUtils.loadImageAsync(this, uri) { handleImage(it) }
        }
      }
      else -> {
        homeScreenStart()
      }
    }
  }

  private fun handleImage(bitmap: Bitmap) {
    val imageByteArray = bitmap.toByteArray()
    val containerId = R.id.fragment_container
    // if current fragment is already ResultFragment just load new data
    // else (is other fragment) replace with newInstance of ResultFragment
    checkFragmentClass<ResultFragment>(containerId,
      { it.loadNewData(imageByteArray) },
      { replaceFragment(containerId, ResultFragment.newInstance(imageByteArray)) }
    )
  }

  companion object {
    private const val TAG = "BaseActivity"

    const val REQUEST_TAKE_IMAGE = 0
  }
}