package com.lndmflngs.colorizer.ui.result

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ResultFragmentProvider {

  @ContributesAndroidInjector
  internal abstract fun provideResultFragmentFactory(): ResultFragment
}