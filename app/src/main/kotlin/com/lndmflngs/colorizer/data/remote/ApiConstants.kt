package com.lndmflngs.colorizer.data.remote

import android.graphics.Bitmap.CompressFormat

object ApiConstants {

  const val URL_IMAGE_COLORIZATION = "deeplearning/ColorfulImageColorization/1.1.13"

  const val HOSTED_DATA_PATH = "data://.my/colorize"

  const val IMAGE_DEF_QUALITY = 100

  const val IMAGE_DEF_FORMAT = "jpg"

  val IMAGE_DEF_COMPRESS_FORMAT = CompressFormat.JPEG
}