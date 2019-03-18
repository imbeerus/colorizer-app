package com.lndmflngs.colorizer.ui.main

import androidx.lifecycle.MutableLiveData
import com.lndmflngs.colorizer.data.DataManager
import com.lndmflngs.colorizer.ui.base.BaseViewModel

class MainViewModel(dataManager: DataManager) : BaseViewModel<MainNavigator>(dataManager) {

  private val imageData: MutableLiveData<ByteArray> = MutableLiveData()

}