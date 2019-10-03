package com.lndmflngs.colorizer

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.lndmflngs.colorizer.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class ColorizerApp : Application(), HasAndroidInjector {

  @Inject
  lateinit var androidInjector: DispatchingAndroidInjector<Any>

  override fun androidInjector(): AndroidInjector<Any> {
    return androidInjector
  }

  override fun onCreate() {
    super.onCreate()

    DaggerAppComponent.builder()
      .application(this)
      .build()
      .inject(this)

    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
  }
}