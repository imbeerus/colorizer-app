package com.lndmflngs.colorizer.ui.main

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import android.util.Log
import androidx.databinding.ObservableField
import com.lndmflngs.colorizer.data.DataManager
import com.lndmflngs.colorizer.extensions.disposableSingleObserver
import com.lndmflngs.colorizer.extensions.subscribeOnApp
import com.lndmflngs.colorizer.ui.base.BaseViewModel

class MainViewModel(dataManager: DataManager) : BaseViewModel<MainNavigator>(dataManager) {

  val title = ObservableField<String>()

  var imageToColorize: ObservableField<ByteArray> = ObservableField()

  fun handleImageSend(intent: Intent) {
    val uri: Uri? = (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)
    if (uri != null) {
      val target = dataManager.makeFutureTarget(uri)
      compositeDisposable.add(
        dataManager.handleImageBitmap(target)
          .subscribeOnApp()
          .subscribeWith(disposableSingleObserver(
            onSuccess = { bitmap ->
              handleImage(bitmap)
              dataManager.clearFutureTarget(target)
            },
            onError = {
              navigator?.handleError(it)
            }
          ))
      )
    }
  }

  fun handleImage(bitmap: Bitmap) {
    imageToColorize.set(dataManager.bitmapToByteArray(bitmap))
    navigator?.showResultFragment()
  }

}