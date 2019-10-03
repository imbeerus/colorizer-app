package com.lndmflngs.colorizer.ui.base

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lndmflngs.colorizer.data.DataManagerHelper
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

abstract class BaseViewModel<N>(
    val dataManager: DataManagerHelper
) : ViewModel() {

    val isLoadingLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val isLoading: ObservableBoolean by lazy {
        ObservableBoolean()
    }

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

    fun setIsLoading(boolean: Boolean) {
        this.isLoading.set(boolean)
        isLoadingLiveData.value = boolean
    }

}