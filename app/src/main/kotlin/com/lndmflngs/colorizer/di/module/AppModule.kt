package com.lndmflngs.colorizer.di.module

import android.app.Application
import android.content.Context
import android.graphics.Bitmap.CompressFormat
import com.lndmflngs.colorizer.BuildConfig
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
import com.lndmflngs.colorizer.di.ImageDefCompressFormat
import com.lndmflngs.colorizer.di.ImageDefFormat
import com.lndmflngs.colorizer.di.ImageDefQuality
import dagger.Module
import dagger.Provides
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
  internal fun provideGlideHelper(glideLoader: GlideLoader): GlideHelper {
    return glideLoader
  }

}