package com.lndmflngs.colorizer.data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.observers.DisposableSingleObserver
import javax.inject.Inject
import javax.inject.Singleton

interface GlideHelper {

  fun loadImageAsync(
    futureTarget: FutureTarget<Bitmap>,
    onSuccess: (Bitmap) -> Unit
  ): SingleObserver<Bitmap>

  fun handleImageBitmap(futureTarget: FutureTarget<Bitmap>): Single<Bitmap>

  fun makeFutureTarget(uri: Uri): FutureTarget<Bitmap>
}

@Singleton
class GlideLoader @Inject
constructor(
  private val context: Context
) : GlideHelper {

  private val TAG = "GlideLoader"

  override fun loadImageAsync(
    futureTarget: FutureTarget<Bitmap>,
    onSuccess: (Bitmap) -> Unit
  ): SingleObserver<Bitmap> {
    return (object : DisposableSingleObserver<Bitmap>() {
      override fun onSuccess(t: Bitmap) {
        onSuccess(t)
        Glide.with(context).clear(futureTarget)
      }

      override fun onError(e: Throwable) {
        Log.d(TAG, " onError: ${e.message}")
      }
    })
  }

  override fun makeFutureTarget(uri: Uri): FutureTarget<Bitmap> {
    return Glide.with(context).asBitmap().load(uri).submit()
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