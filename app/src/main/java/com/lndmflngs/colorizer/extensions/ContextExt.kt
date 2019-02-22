package com.lndmflngs.colorizer.extensions

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.lndmflngs.colorizer.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun Context.drawable(res: Int): Drawable? = ContextCompat.getDrawable(this, res)

fun Context.galleryAddPic(imageFile: File) {
  Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
    mediaScanIntent.data = Uri.fromFile(imageFile)
    sendBroadcast(mediaScanIntent)
  }
}

fun Context.galleryAddPic(imagePath: String) = galleryAddPic(File(imagePath))

fun Context.getMediaBitmap(uri: Uri): Bitmap =
  MediaStore.Images.Media.getBitmap(contentResolver, uri)

fun Context.getBitmapUri(imageView: ImageView): Uri? {
  // Extract Bitmap from ImageView drawable
  val drawable = imageView.drawable
  val bmp = if (drawable is BitmapDrawable) {
    (imageView.drawable as BitmapDrawable).bitmap
  } else {
    null
  }
  // Store image to default external storage directory
  try {
    val file =
      File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${System.currentTimeMillis()}.png")
    val out = FileOutputStream(file)
    bmp!!.compress(Bitmap.CompressFormat.PNG, 100, out)
    out.close()

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      FileProvider.getUriForFile(this, getString(R.string.file_provider_authority), file)
    } else {
      Uri.fromFile(file)
    }
  } catch (e: IOException) {
    e.printStackTrace()
  }
  return null
}

fun Context.toast(str: String, duration: Int = Toast.LENGTH_SHORT) =
  Toast.makeText(this, str, duration).show()

fun Context.toast(strId: Int, duration: Int = Toast.LENGTH_SHORT) =
  Toast.makeText(this, getString(strId), duration).show()