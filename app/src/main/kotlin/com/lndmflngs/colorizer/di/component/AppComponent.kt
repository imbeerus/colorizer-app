package com.lndmflngs.colorizer.di.component

import android.app.Application
import com.lndmflngs.colorizer.ColorizerApp
import com.lndmflngs.colorizer.di.builder.ActivityBuilder
import com.lndmflngs.colorizer.di.module.ApiModule
import com.lndmflngs.colorizer.di.module.AppModule
import com.lndmflngs.colorizer.di.module.ImageManagerModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    ApiModule::class,
    ImageManagerModule::class,
    ActivityBuilder::class
  ]
)
interface AppComponent {

  fun inject(app: ColorizerApp)

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun application(application: Application): Builder

    fun build(): AppComponent
  }
}