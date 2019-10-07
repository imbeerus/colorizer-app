package com.lndmflngs.colorizer.data.local

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.core.graphics.decodeBitmap
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.di.ImageDefCompressFormat
import com.lndmflngs.colorizer.di.ImageDefFormat
import com.lndmflngs.colorizer.di.ImageDefQuality
import com.lndmflngs.colorizer.extensions.displayMetrics
import io.reactivex.Single
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

interface ImageManagerHelper {

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray

    fun scaleBitmapToDevice(bitmap: Bitmap): ByteArray

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

    override fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(defCompressFormat, quality, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    override fun scaleBitmapToDevice(bitmap: Bitmap): ByteArray {
        val scaledWidth = bitmap.getScaledWidth(context.displayMetrics)
        val scaledHeight = bitmap.getScaledHeight(context.displayMetrics)
        val scaled = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, false)
        return bitmapToByteArray(scaled)
    }

    override fun getMediaBitmap(uri: Uri): Bitmap {
        return ImageDecoder.createSource(context.contentResolver, uri).decodeBitmap { _, _ -> }
    }

    override fun getImageBitmapUri(imageView: ImageView): Single<Uri> {
        return Single.create {
            try {
                val bmp = imageView.extractBitmap()
                val fileName = "${System.currentTimeMillis()}"
                val uri: Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val resolver = context.contentResolver
                    uri = resolver.fetchUri(fileName)
                    resolver.writeBitmap(bmp, uri)
                } else {
                    val file =
                        File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                            "$fileName.$defFormat"
                        )
                    file.writeBitmap(bmp)
                    uri = file.fetchUri()
                }
                it.onSuccess(uri)
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }


    private fun ImageView.extractBitmap(): Bitmap? {
        return if (this.drawable is BitmapDrawable) {
            (this.drawable as BitmapDrawable).bitmap
        } else {
            null
        }
    }

    private fun File.writeBitmap(bitmap: Bitmap?) {
        val out = FileOutputStream(this)
        bitmap?.compress(defCompressFormat, quality, out)
        out.close()
    }

    private fun File.fetchUri(): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                context.getString(R.string.file_provider_authority),
                this
            )
        } else {
            Uri.fromFile(this)
        }
    }

    private fun ContentResolver.writeBitmap(bmp: Bitmap?, uri: Uri?) {
        if (bmp != null && uri != null) {
            var stream: OutputStream? = null
            try {
                openOutputStream(uri).use { outputStream ->
                    stream = outputStream
                    outputStream?.write(bitmapToByteArray(bmp))
                }
            } finally {
                stream?.close()
            }
        }
    }

    private fun ContentResolver.fetchUri(fileName: String): Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/$defFormat")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/Colorizer")
        }
        return insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )!!
    }


}