package com.lndmflngs.colorizer.ui.main

interface MainNavigator {
  
  fun handleImageIntent(defaultStart: () -> Unit)

  fun showResultFragment()

  fun handleError(throwable: Throwable)

}