package com.lndmflngs.colorizer.extensions

import android.util.Log
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

private const val TAG = "RxExt"

fun <T> Single<T>.subscribeOnApp(): Single<T> {
  return this@subscribeOnApp
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
}

fun <T> singleObserver(
  onSuccess: (T) -> Unit,
  onSubscribe: (Disposable) -> Unit,
  onError: (Throwable) -> Unit
): SingleObserver<T> {
  return object : SingleObserver<T> {
    override fun onSuccess(t: T) {
      Log.d(TAG, "onSuccess: $t")
      onSuccess(t)
    }

    override fun onSubscribe(d: Disposable) {
      Log.d(TAG, "onSubscribe: ${d.isDisposed}")
      onSubscribe(d)
    }

    override fun onError(e: Throwable) {
      Log.d(TAG, "onError: ${e.printStackTrace()}")
      onError(e)
    }
  }
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