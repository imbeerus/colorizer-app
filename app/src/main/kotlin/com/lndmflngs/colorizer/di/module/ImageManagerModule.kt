package com.lndmflngs.colorizer.di.module

import android.graphics.Bitmap.CompressFormat
import com.lndmflngs.colorizer.data.local.ImageManager
import com.lndmflngs.colorizer.data.local.ImageManagerHelper
import com.lndmflngs.colorizer.data.remote.ApiConstants
import com.lndmflngs.colorizer.di.ImageDefCompressFormat
import com.lndmflngs.colorizer.di.ImageDefFormat
import com.lndmflngs.colorizer.di.ImageDefQuality
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ImageManagerModule {

  @Provides
  @Singleton
  internal fun provideImageManagerHelper(imageManager: ImageManager): ImageManagerHelper {
    return imageManager
  }

  @Provides
  @ImageDefCompressFormat
  internal fun provideImageDefCompressFormat(): CompressFormat {
    return ApiConstants.IMAGE_DEF_COMPRESS_FORMAT
  }

  @Provides
  @ImageDefFormat
  internal fun provideImageDefFormat(): String {
    return ApiConstants.IMAGE_DEF_FORMAT
  }

  @Provides
  @ImageDefQuality
  internal fun provideImageDefQuality(): Int {
    return ApiConstants.IMAGE_DEF_QUALITY
  }

}