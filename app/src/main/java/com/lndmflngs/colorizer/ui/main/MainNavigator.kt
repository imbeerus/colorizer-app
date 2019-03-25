package com.lndmflngs.colorizer.ui.main

interface MainNavigator {

  fun showResultFragment()

  fun handleIntent(homeScreenStart: () -> Unit)

  fun handleError(throwable: Throwable)

}