package com.lndmflngs.colorizer.ui.main.open

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class OpenFragmentProvider {

  @ContributesAndroidInjector
  internal abstract fun provideOpenFragmentFactory(): OpenFragment
}