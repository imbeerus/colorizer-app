package com.lndmflngs.colorizer.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

inline fun <reified T : ViewModel> Fragment.getViewModel(factory: ViewModelProvider.Factory? = null): T {
  return if (factory == null) {
    ViewModelProviders.of(this).get(T::class.java)
  } else {
    ViewModelProviders.of(this, factory).get(T::class.java)
  }
}

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(factory: ViewModelProvider.Factory? = null): T {
  return if (factory == null) {
    ViewModelProviders.of(this).get(T::class.java)
  } else {
    ViewModelProviders.of(this, factory).get(T::class.java)
  }
}