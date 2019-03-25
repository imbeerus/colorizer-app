package com.lndmflngs.colorizer.ui.main

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import androidx.databinding.ObservableField
import com.lndmflngs.colorizer.data.DataManager
import com.lndmflngs.colorizer.ui.base.BaseViewModel

class MainViewModel(dataManager: DataManager) : BaseViewModel<MainNavigator>(dataManager) {

  val title = ObservableField<String>()

  lateinit var imageToColorize: ByteArray
    private set

  fun handleImageSend(intent: Intent) {
    val uri: Uri? = (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri) //TODO: handle image
//    GlideUtils.loadImageAsync(this, uri) {
//    handleImage(it)
// }

  }

  fun handleImage(bitmap: Bitmap) {
    imageToColorize = dataManager.bitmapToByteArray(bitmap)
    navigator?.showResultFragment()
  }

}