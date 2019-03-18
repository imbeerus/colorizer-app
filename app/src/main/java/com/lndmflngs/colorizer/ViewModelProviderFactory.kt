package com.lndmflngs.colorizer

import com.lndmflngs.colorizer.ui.main.MainViewModel
import androidx.lifecycle.ViewModel
import com.lndmflngs.colorizer.data.DataManager
import javax.inject.Inject
import androidx.lifecycle.ViewModelProvider
import com.lndmflngs.colorizer.ui.main.about.AboutViewModel
import com.lndmflngs.colorizer.ui.main.open.OpenViewModel
import com.lndmflngs.colorizer.ui.main.result.ResultViewModel
import javax.inject.Singleton

@Suppress("UNCHECKED_CAST")
@Singleton
class ViewModelProviderFactory @Inject
constructor(
  private val dataManager: DataManager
) : ViewModelProvider.NewInstanceFactory() {

  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return when {
      modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(dataManager) as T
      modelClass.isAssignableFrom(OpenViewModel::class.java) -> OpenViewModel(dataManager) as T
      modelClass.isAssignableFrom(ResultViewModel::class.java) -> ResultViewModel(dataManager) as T
      modelClass.isAssignableFrom(AboutViewModel::class.java) -> AboutViewModel(dataManager) as T
      else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
  }
}