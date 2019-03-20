package com.lndmflngs.colorizer.ui.result

import android.net.Uri

interface ResultNavigator {

  fun startPickImage()

  fun startShareImage(uri: Uri)

  fun updateMenu()

  fun handleError(throwable: Throwable)

}