package com.lndmflngs.colorizer.ui.main

interface MainNavigator {

  fun showOpenFragment()

  fun showResultFragment()

  fun handleError(throwable: Throwable)

}