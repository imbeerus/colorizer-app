package com.lndmflngs.colorizer

import android.app.Activity
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.lndmflngs.colorizer.di.component.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

// TODO: upload new screenshots
class ColorizerApp : Application(), HasActivityInjector {

  @Inject
  internal lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

  override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
    return activityDispatchingAndroidInjector
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