package com.lndmflngs.colorizer.ui.result

import android.widget.ImageView
import androidx.databinding.ObservableBoolean
import com.lndmflngs.colorizer.data.DataManager
import com.lndmflngs.colorizer.extensions.disposableSingleObserver
import com.lndmflngs.colorizer.extensions.subscribeOnApp
import com.lndmflngs.colorizer.ui.base.BaseViewModel

class ResultViewModel(dataManager: DataManager) : BaseViewModel<ResultNavigator>(dataManager) {

  val isMenuActionsEnabled = ObservableBoolean()

  lateinit var imageView: ImageView
  lateinit var resultImageSource: String

  fun sendImageToColorize(byteArray: ByteArray) {
    setIsLoading(true)
    isMenuActionsEnabled.set(false)
    compositeDisposable.add(
      dataManager.colorizeImageRequest(byteArray)
        .subscribeOnApp()
        .subscribeWith(disposableSingleObserver(
          onSuccess = {
            fetchResult(it.output)
          },
          onError = {
            setIsLoading(false)
            navigator?.handleError(it)
          }
        ))
    )
  }

  private fun fetchResult(output: String) {
    compositeDisposable.add(
      dataManager.fetchResultImagePath(output)
        .subscribeOnApp()
        .subscribeWith(disposableSingleObserver(
          onSuccess = {
            setIsLoading(false)
            isMenuActionsEnabled.set(true)
            resultImageSource = it
            navigator?.showResult()
          },
          onError = {
            setIsLoading(false)
            navigator?.handleError(it)
          }
        ))
    )
  }

  fun shareImage() {
    val bmpUri = dataManager.getImageBitmapUri(imageView)
    navigator?.startShareImage(bmpUri!!)
  }

}