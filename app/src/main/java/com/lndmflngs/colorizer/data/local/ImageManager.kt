package com.lndmflngs.colorizer.data.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import androidx.core.content.FileProvider
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.di.ImageDefCompressFormat
import com.lndmflngs.colorizer.di.ImageDefFormat
import com.lndmflngs.colorizer.di.ImageDefQuality
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

interface ImageManagerHelper {

  fun bitmapToByteArray(bitmap: Bitmap): ByteArray

  fun getMediaBitmap(uri: Uri): Bitmap

  fun getImageBitmapUri(imageView: ImageView): Uri?
}

@Singleton
class ImageManager @Inject
constructor(
  private val context: Context,
  @ImageDefFormat private val defFormat: String,
  @ImageDefCompressFormat private val defCompressFormat: CompressFormat,
  @ImageDefQuality private val quality: Int
) : ImageManagerHelper {

  override fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(defCompressFormat, quality, byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray()
  }

  override fun getMediaBitmap(uri: Uri): Bitmap {
    return MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
  }

  override fun getImageBitmapUri(imageView: ImageView): Uri? {
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
        File(
          context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
          "${System.currentTimeMillis()}.$defFormat"
        )
      val out = FileOutputStream(file)
      bmp!!.compress(defCompressFormat, quality, out)
      out.close()
      return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(
          context,
          context.getString(R.string.file_provider_authority),
          file
        )
      } else {
        Uri.fromFile(file)
      }
    } catch (e: IOException) {
      e.printStackTrace()
    }
    return null
  }

}