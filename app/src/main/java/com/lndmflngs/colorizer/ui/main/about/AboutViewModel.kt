package com.lndmflngs.colorizer.ui.main.about

import com.lndmflngs.colorizer.data.DataManager
import com.lndmflngs.colorizer.ui.base.BaseViewModel

class AboutViewModel(dataManager: DataManager) : BaseViewModel<AboutCallback>(dataManager) {

  fun onLaterClick() {
    navigator?.dismissDialog()
  }

  fun onSubmitClick() {
    navigator?.dismissDialog()
  }
}