package com.lndmflngs.colorizer.data

import android.graphics.Bitmap
import android.net.Uri
import com.bumptech.glide.request.FutureTarget
import com.lndmflngs.colorizer.data.local.FileManagerHelper
import com.lndmflngs.colorizer.data.local.GlideHelper
import com.lndmflngs.colorizer.data.model.api.ImageResponse
import com.lndmflngs.colorizer.data.remote.ApiClientHelper
import io.reactivex.Single
import io.reactivex.SingleObserver
import javax.inject.Inject
import javax.inject.Singleton

interface DataManagerHelper : ApiClientHelper, GlideHelper, FileManagerHelper

@Singleton
class DataManager @Inject
constructor(
  private val apiClient: ApiClientHelper,
  private val fileManager: FileManagerHelper,
  private val glideLoader: GlideHelper
) : DataManagerHelper {

  override fun doColorizeImageRequest(input: String): Single<ImageResponse> {
    return apiClient.doColorizeImageRequest(input)
  }

  override fun doColorizeImage(input: ByteArray): Single<ImageResponse> {
    return apiClient.doColorizeImage(input)
  }

  override fun fetchResultImagePath(output: String): Single<String> {
    return apiClient.fetchResultImagePath(output)
  }

  override fun makeFutureTarget(uri: Uri): FutureTarget<Bitmap> {
    return glideLoader.makeFutureTarget(uri)
  }

  override fun handleImageBitmap(futureTarget: FutureTarget<Bitmap>): Single<Bitmap> {
    return glideLoader.handleImageBitmap(futureTarget)
  }

  override fun loadImageAsync(
    futureTarget: FutureTarget<Bitmap>,
    onSuccess: (Bitmap) -> Unit
  ): SingleObserver<Bitmap> {
    return glideLoader.loadImageAsync(futureTarget, onSuccess)
  }

}