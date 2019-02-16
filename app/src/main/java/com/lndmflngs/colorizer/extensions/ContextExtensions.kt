package com.lndmflngs.colorizer.extensions

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.io.File

fun Context.drawable(res: Int): Drawable? = ContextCompat.getDrawable(this, res)

fun Context.galleryAddPic(image: File) {
    Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
        mediaScanIntent.data = Uri.fromFile(image)
        sendBroadcast(mediaScanIntent)
    }
}

fun Context.galleryAddPic(imagePath: String) = galleryAddPic(File(imagePath))

fun Context.toast(str: String, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, str, duration).show()