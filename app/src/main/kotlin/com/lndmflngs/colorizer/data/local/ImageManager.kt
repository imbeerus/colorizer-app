package com.lndmflngs.colorizer.data.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.widget.ImageView
import androidx.core.content.FileProvider
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.di.ImageDefCompressFormat
import com.lndmflngs.colorizer.di.ImageDefFormat
import com.lndmflngs.colorizer.di.ImageDefQuality
import io.reactivex.Single
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

interface ImageManagerHelper {

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray

    fun encodedImage(byteArray: ByteArray): String

    fun getMediaBitmap(uri: Uri): Bitmap

    fun getImageBitmapUri(imageView: ImageView): Single<Uri>

}

@Singleton
class ImageManager @Inject
constructor(
    private val context: Context,
    @ImageDefFormat private val defFormat: String,
    @ImageDefCompressFormat private val defCompressFormat: CompressFormat,
    @ImageDefQuality private val quality: Int
) : ImageManagerHelper {

    override fun encodedImage(byteArray: ByteArray): String {
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    override fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(defCompressFormat, quality, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    override fun getMediaBitmap(uri: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    }

    override fun getImageBitmapUri(imageView: ImageView): Single<Uri> {
        return Single.create {
            try {
                val bmp = extractBitmap(imageView)
                // Store image to public external storage directory
                val file =
                    File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "${System.currentTimeMillis()}.$defFormat"
                    )
                writeBitmap(bmp, file)

                val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    FileProvider.getUriForFile(
                        context,
                        context.getString(R.string.file_provider_authority),
                        file
                    )
                } else {
                    Uri.fromFile(file)
                }
                it.onSuccess(uri)
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }

    private fun extractBitmap(imageView: ImageView): Bitmap? {
        // Extract Bitmap from ImageView drawable
        return if (imageView.drawable is BitmapDrawable) {
            (imageView.drawable as BitmapDrawable).bitmap
        } else {
            null
        }
    }

    private fun writeBitmap(bitmap: Bitmap?, file: File) {
        val out = FileOutputStream(file)
        bitmap!!.compress(defCompressFormat, quality, out)
        out.close()
    }

}