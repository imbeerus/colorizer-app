package com.lndmflngs.colorizer.extensions

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import java.io.ByteArrayOutputStream

fun Bitmap.toByteArray(
  format: CompressFormat = Bitmap.CompressFormat.JPEG,
  quality: Int = 100
): ByteArray {
  val byteArrayOutputStream = ByteArrayOutputStream()
  compress(format, quality, byteArrayOutputStream)
  return byteArrayOutputStream.toByteArray()
}