package com.lndmflngs.colorizer.extensions

import android.util.Log
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

private const val TAG = "RxExt"

fun <T> Single<T>.subscribeOnApp(): Single<T> {
  return this@subscribeOnApp
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
}

fun <T> disposableSingleObserver(
  onSuccess: (T) -> Unit,
  onError: (Throwable) -> Unit
): DisposableSingleObserver<T> {
  return object : DisposableSingleObserver<T>() {
    override fun onSuccess(t: T) {
      Log.d(TAG, "onSuccess: $t")
      onSuccess(t)
    }

    override fun onError(e: Throwable) {
      Log.d(TAG, "onError: ${e.printStackTrace()}")
      onError(e)
    }
  }
}