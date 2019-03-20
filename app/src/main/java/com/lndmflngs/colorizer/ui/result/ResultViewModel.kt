package com.lndmflngs.colorizer.ui.result

import android.widget.ImageView
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.lndmflngs.colorizer.data.DataManager
import com.lndmflngs.colorizer.extensions.disposableSingleObserver
import com.lndmflngs.colorizer.extensions.subscribeOnApp
import com.lndmflngs.colorizer.ui.base.BaseViewModel

class ResultViewModel(dataManager: DataManager) : BaseViewModel<ResultNavigator>(dataManager) {

  val resultImageData: MutableLiveData<ByteArray> = MutableLiveData()
  val imageUrl: MutableLiveData<String> = MutableLiveData()
  val isMenuActionsEnabled = ObservableBoolean()

  lateinit var imageView: ImageView

  fun setImageSource(source: ImageView) {
    imageView = source
  }

  fun shareImage() {
    // val bmpUri = dataManager.getImageBitmapUri(resultImageView)
//    navigator?.startShareImage()
  }

  fun sendImageToColorize() {
    setIsLoading(true)
    isMenuActionsEnabled.set(false)
    compositeDisposable.add(
      dataManager.doColorizeImage(resultImageData.value!!)
        .subscribeOnApp()
        .subscribeWith(disposableSingleObserver(
          onSuccess = {
            setIsLoading(false)
            isMenuActionsEnabled.set(true)
            navigator?.updateMenu()
          },
          onError = {
            setIsLoading(false)
            navigator?.handleError(it)
          }
        ))
    )
  }

}