package com.lndmflngs.colorizer.ui.result

import android.net.Uri

interface ResultNavigator {

    fun startShareImage(uri: Uri)

    fun showResult()

    fun handleError(throwable: Throwable)
}