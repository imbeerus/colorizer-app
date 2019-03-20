package com.lndmflngs.colorizer.di.builder

import com.lndmflngs.colorizer.ui.main.MainActivity
import com.lndmflngs.colorizer.ui.about.AboutDialogProvider
import com.lndmflngs.colorizer.ui.open.OpenFragmentProvider
import com.lndmflngs.colorizer.ui.result.ResultFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

  @ContributesAndroidInjector(modules = [OpenFragmentProvider::class, ResultFragmentProvider::class, AboutDialogProvider::class])
  internal abstract fun bindMainActivity(): MainActivity

}