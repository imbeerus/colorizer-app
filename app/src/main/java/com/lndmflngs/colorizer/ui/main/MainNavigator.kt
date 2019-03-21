package com.lndmflngs.colorizer.ui.main

interface MainNavigator {

  fun showAboutDialog()

  fun showOpenFragment()

  fun showResultFragment()

  fun handleIntent(homeScreenStart: () -> Unit)

  fun handleError(throwable: Throwable)

}