package com.lndmflngs.colorizer.ui.result

import android.widget.ImageView
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.lndmflngs.colorizer.data.DataManager
import com.lndmflngs.colorizer.extensions.disposableSingleObserver
import com.lndmflngs.colorizer.extensions.subscribeOnApp
import com.lndmflngs.colorizer.ui.base.BaseViewModel

class ResultViewModel(dataManager: DataManager) : BaseViewModel<ResultNavigator>(dataManager) {

    val imageSource: ObservableField<String> = ObservableField()

    val isMenuActionsEnabled = ObservableBoolean()

    init {
        dataManager.imageToColorize?.let {
            val byteArray = dataManager.bitmapToByteArray(it)
            sendImageToColorize(byteArray)
        }
    }

    private fun sendImageToColorize(byteArray: ByteArray) {
        setIsLoading(true)
        isMenuActionsEnabled.set(false)
        compositeDisposable.add(
            dataManager.colorizeImageRequest(byteArray)
                .subscribeOnApp()
                .subscribeWith(disposableSingleObserver(
                    onSuccess = { fetchResult(it.output) },
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
                        imageSource.set(it)
                        navigator?.showResult()
                    },
                    onError = {
                        setIsLoading(false)
                        navigator?.handleError(it)
                    }
                ))
        )
    }

    fun shareImage(imageView: ImageView) {
        compositeDisposable.add(
            dataManager.getImageBitmapUri(imageView)
                .subscribeOnApp()
                .subscribeWith(disposableSingleObserver(
                    onSuccess = { navigator?.startShareImage(it) },
                    onError = { navigator?.handleError(it) }
                ))
        )
    }

    override fun onCleared() {
        super.onCleared()
        dataManager.imageToColorize = null
    }
}