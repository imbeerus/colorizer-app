package com.lndmflngs.colorizer.ui.about

import com.lndmflngs.colorizer.AboutDialog
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AboutDialogProvider {

  @ContributesAndroidInjector
  internal abstract fun provideAboutDialogFactory(): AboutDialog
}
