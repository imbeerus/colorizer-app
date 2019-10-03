package com.lndmflngs.colorizer.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Получение ViewModel в Fragment исходя из [ViewModelProvider.Factory]
 * @param T тип передаваемый ViewModel
 * @param factory передаваемый factory
 * @return [ViewModel] типа [T]
 */
inline fun <reified T : ViewModel> Fragment.getViewModel(factory: ViewModelProvider.Factory? = null): T {
  return if (factory == null) {
    ViewModelProvider(this).get(T::class.java)
  } else {
    ViewModelProvider(this, factory).get(T::class.java)
  }
}

/**
 * Получение ViewModel в FragmentActivity исходя из [ViewModelProvider.Factory]
 * @param T тип передаваемый ViewModel
 * @param factory передаваемый factory
 * @return [ViewModel] типа [T]
 */
inline fun <reified T : ViewModel> FragmentActivity.getViewModel(factory: ViewModelProvider.Factory? = null): T {
  return if (factory == null) {
    ViewModelProvider(this).get(T::class.java)
  } else {
    ViewModelProvider(this, factory).get(T::class.java)
  }
}