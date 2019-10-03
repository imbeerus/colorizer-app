package com.lndmflngs.colorizer.di.module

import com.algorithmia.Algorithmia
import com.algorithmia.AlgorithmiaClient
import com.algorithmia.algo.Algorithm
import com.lndmflngs.colorizer.BuildConfig
import com.lndmflngs.colorizer.data.remote.ApiClient
import com.lndmflngs.colorizer.data.remote.ApiClientHelper
import com.lndmflngs.colorizer.data.remote.ApiConstants
import com.lndmflngs.colorizer.di.ApiKey
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApiModule {

  @Provides
  @ApiKey
  internal fun provideApiKey(): String {
    return BuildConfig.API_KEY
  }

  @Provides
  @Singleton
  internal fun provideAlgoClient(@ApiKey apiKey: String): AlgorithmiaClient {
    return Algorithmia.client(apiKey)
  }

  @Provides
  @Singleton
  internal fun provideColorizerAlgorithm(algoClient: AlgorithmiaClient): Algorithm {
    return algoClient.algo(ApiConstants.URL_IMAGE_COLORIZATION)
  }

  @Provides
  @Singleton
  internal fun provideApiClientHelper(apiClient: ApiClient): ApiClientHelper {
    return apiClient
  }

}