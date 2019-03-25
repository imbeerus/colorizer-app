package com.lndmflngs.colorizer.ui.about

import androidx.databinding.ObservableField
import com.lndmflngs.colorizer.data.DataManager
import com.lndmflngs.colorizer.ui.base.BaseViewModel

class AboutViewModel(dataManager: DataManager) : BaseViewModel<AboutCallback>(dataManager) {

  val appVersion: ObservableField<String> = ObservableField()

  fun openRepoInBrowser(){
    navigator?.openRepo()
  }

  fun onLaterClick() {
    navigator?.dismissDialog()
  }

  fun onSubmitClick() {
    navigator?.dismissDialog()
  }

  companion object {
    const val REPO_URL = "https://github.com/lndmflngs/colorizer-app"
    const val TAG = "AboutDialog"
  }
}