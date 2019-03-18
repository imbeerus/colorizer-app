package com.lndmflngs.colorizer.ui.main.about

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AboutDialogProvider {

  @ContributesAndroidInjector
  internal abstract fun provideAboutDialogFactory(): AboutDialog
}
