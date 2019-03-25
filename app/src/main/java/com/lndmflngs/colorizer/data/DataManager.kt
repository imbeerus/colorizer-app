package com.lndmflngs.colorizer.data

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.request.FutureTarget
import com.lndmflngs.colorizer.data.local.ImageManagerHelper
import com.lndmflngs.colorizer.data.model.api.ImageResponse
import com.lndmflngs.colorizer.data.remote.ApiClientHelper
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

interface DataManagerHelper : ApiClientHelper, GlideHelper, ImageManagerHelper

@Singleton
class DataManager @Inject
constructor(
  private val apiClient: ApiClientHelper,
  private val imageManager: ImageManagerHelper,
  private val glideLoader: GlideHelper
) : DataManagerHelper {

  override fun colorizeImageRequest(input: ByteArray): Single<ImageResponse> {
    return apiClient.colorizeImageRequest(input)
  }

  override fun fetchResultImagePath(output: String): Single<String> {
    return apiClient.fetchResultImagePath(output)
  }

  override fun clearFutureTarget(futureTarget: FutureTarget<Bitmap>) {
    glideLoader.clearFutureTarget(futureTarget)
  }

  override fun makeFutureTarget(uri: Uri): FutureTarget<Bitmap> {
    return glideLoader.makeFutureTarget(uri)
  }

  override fun handleImageBitmap(futureTarget: FutureTarget<Bitmap>): Single<Bitmap> {
    return glideLoader.handleImageBitmap(futureTarget)
  }

  override fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    return imageManager.bitmapToByteArray(bitmap)
  }

  override fun getMediaBitmap(uri: Uri): Bitmap {
    return imageManager.getMediaBitmap(uri)
  }

  override fun getImageBitmapUri(imageView: ImageView): Uri? {
    return imageManager.getImageBitmapUri(imageView)
  }

}