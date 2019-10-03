package com.lndmflngs.colorizer.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ImageDefQuality

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ImageDefFormat

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ImageDefCompressFormat