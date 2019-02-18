package com.lndmflngs.colorizer.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.extensions.replaceFragment
import com.lndmflngs.colorizer.extensions.toast
import com.lndmflngs.colorizer.ui.fragments.ResultFragment
import kotlinx.android.synthetic.main.include_toolbar.toolbar
import java.io.ByteArrayOutputStream

abstract class BaseActivity : AppCompatActivity() {

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    initToolbar()
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
      val imageUri = data?.data
      val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
      val imageByteArray = bitmapToByteArray(imageBitmap)
      replaceFragment(R.id.fragment_container, ResultFragment.newInstance(imageByteArray))
    }
  }

  private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray()
  }

  private fun initToolbar() {
    setSupportActionBar(toolbar)
    val actionbar: ActionBar? = supportActionBar
    actionbar?.apply {
      setDisplayHomeAsUpEnabled(true)
      setHomeAsUpIndicator(R.drawable.ic_leaves)
    }
  }

  companion object {
    const val REQUEST_TAKE_IMAGE = 0
  }
}