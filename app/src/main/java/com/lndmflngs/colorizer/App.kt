package com.lndmflngs.colorizer

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.lndmflngs.colorizer.extensions.DelegatesExt

class App : MultiDexApplication() {

  companion object {
    var instance: App by DelegatesExt.notNullSingleValue()
  }

  override fun onCreate() {
    super.onCreate()
    instance = this
  }

  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(base)
    MultiDex.install(this)
  }

}