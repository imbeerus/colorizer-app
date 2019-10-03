package com.lndmflngs.colorizer.data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.bumptech.glide.request.FutureTarget
import com.lndmflngs.colorizer.glide.GlideApp
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

interface GlideHelper {

  fun clearFutureTarget(futureTarget: FutureTarget<Bitmap>)

  fun handleImageBitmap(futureTarget: FutureTarget<Bitmap>): Single<Bitmap>

  fun makeFutureTarget(uri: Uri): FutureTarget<Bitmap>
}

@Singleton
class GlideLoader @Inject
constructor(
  private val context: Context
) : GlideHelper {

  override fun clearFutureTarget(futureTarget: FutureTarget<Bitmap>) {
    GlideApp.with(context).clear(futureTarget)
  }

  override fun makeFutureTarget(uri: Uri): FutureTarget<Bitmap> {
    return GlideApp.with(context).asBitmap().load(uri).submit()
  }

  override fun handleImageBitmap(futureTarget: FutureTarget<Bitmap>): Single<Bitmap> {
    return Single.create {
      try {
        val bitmap = futureTarget.get()
        it.onSuccess(bitmap)
      } catch (e: Exception) {
        it.onError(e)
      }
    }
  }

}