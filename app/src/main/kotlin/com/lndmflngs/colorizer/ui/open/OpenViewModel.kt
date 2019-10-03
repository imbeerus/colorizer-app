package com.lndmflngs.colorizer.ui.open

import com.lndmflngs.colorizer.data.DataManager
import com.lndmflngs.colorizer.ui.base.BaseViewModel

class OpenViewModel(dataManager: DataManager) : BaseViewModel<OpenNavigator>(dataManager) {

  fun pickImage() {
    navigator?.startPickImage()
  }

}