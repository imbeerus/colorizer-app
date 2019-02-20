package com.lndmflngs.colorizer.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

object GlideUtils {

  private const val TAG = "GlideUtils"

  fun loadImageAsync(context: Context, uri: Uri, onSuccess: (Bitmap) -> Unit) {
    val futureTarget = Glide.with(context).asBitmap().load(uri).submit()
    handleImageBitmap(futureTarget)
      .subscribeOn(Schedulers.single())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(object : SingleObserver<Bitmap> {
        override fun onSuccess(t: Bitmap) {
          onSuccess(t)
          Glide.with(context).clear(futureTarget)
        }

        override fun onSubscribe(d: Disposable) {
          Log.d(TAG, " onSubscribe : " + d.isDisposed)
        }

        override fun onError(e: Throwable) {
          Log.d(TAG, " onError : " + e.message)
        }
      })
  }

  fun loadImageAsync(fragment: Fragment, string: String, onSuccess: (Bitmap) -> Unit) {
    loadImageAsync(fragment.requireContext(), Uri.parse(string)) { onSuccess(it) }
  }

  private fun handleImageBitmap(futureTarget: FutureTarget<Bitmap>): Single<Bitmap> {
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