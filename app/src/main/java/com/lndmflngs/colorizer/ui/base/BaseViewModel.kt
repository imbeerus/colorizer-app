package com.lndmflngs.colorizer.ui.base

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.lndmflngs.colorizer.data.DataManagerHelper
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

abstract class BaseViewModel<N>(
  val dataManager: DataManagerHelper
) : ViewModel() {

  val isLoading = ObservableBoolean()

  val compositeDisposable: CompositeDisposable = CompositeDisposable()

  private lateinit var navigatorReference: WeakReference<N>

  var navigator: N?
    get() = navigatorReference.get()
    set(navigator) {
      this.navigatorReference = WeakReference<N>(navigator)
    }

  override fun onCleared() {
    compositeDisposable.dispose()
    super.onCleared()
  }

  fun setIsLoading(isLoading: Boolean) {
    this.isLoading.set(isLoading)
  }
}