package com.lndmflngs.colorizer.di.module

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lndmflngs.colorizer.BuildConfig
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.data.DataManager
import com.lndmflngs.colorizer.data.DataManagerHelper
import com.lndmflngs.colorizer.data.GlideHelper
import com.lndmflngs.colorizer.data.GlideLoader
import com.lndmflngs.colorizer.data.local.ImageManager
import com.lndmflngs.colorizer.data.local.ImageManagerHelper
import com.lndmflngs.colorizer.data.remote.ApiClient
import com.lndmflngs.colorizer.data.remote.ApiClientHelper
import com.lndmflngs.colorizer.data.remote.ApiConstants
import com.lndmflngs.colorizer.di.ApiKey
import com.lndmflngs.colorizer.di.ImageDefFormat
import dagger.Module
import dagger.Provides
import io.github.inflationx.calligraphy3.CalligraphyConfig
import javax.inject.Singleton

@Module
class AppModule {

  @Provides
  @Singleton
  fun provideContext(application: Application): Context {
    return application
  }

  @Provides
  @Singleton
  internal fun provideDataManagerHelper(dataManager: DataManager): DataManagerHelper {
    return dataManager
  }

  @Provides
  @Singleton
  internal fun provideCalligraphyDefaultConfig(): CalligraphyConfig {
    return CalligraphyConfig.Builder()
      .setDefaultFontPath("fonts/Roboto-Regular.ttf")
      .setFontAttrId(R.attr.fontPath)
      .build()
  }

  // ApiClient

  @Provides
  @Singleton
  internal fun provideApiClientHelper(apiClient: ApiClient): ApiClientHelper {
    return apiClient
  }

  @Provides
  @Singleton
  fun provideGson(): Gson {
    return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
  }

  @Provides
  @ApiKey
  internal fun provideApiKey(): String {
    return BuildConfig.API_KEY
  }

  // ImageManager

  @Provides
  @Singleton
  internal fun provideImageManagerHelper(imageManager: ImageManager): ImageManagerHelper {
    return imageManager
  }

  @Provides
  @ImageDefFormat
  internal fun provideDefImageFormat(): String {
    return ApiConstants.DEF_IMG_FORMAT
  }

  // Glide

  @Provides
  @Singleton
  internal fun provideGlideHelper(glideLoader: GlideLoader): GlideHelper {
    return glideLoader
  }

}