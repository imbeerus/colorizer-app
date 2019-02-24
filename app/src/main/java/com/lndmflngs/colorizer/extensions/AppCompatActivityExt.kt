package com.lndmflngs.colorizer.extensions

import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun AppCompatActivity.setupActionBar(toolbar: Toolbar, action: ActionBar.() -> Unit) {
  setSupportActionBar(toolbar)
  supportActionBar?.run {
    action()
  }
}

fun AppCompatActivity.replaceFragment(@IdRes containerId: Int, fragment: Fragment) {
  supportFragmentManager.transact { replace(containerId, fragment) }
}

fun AppCompatActivity.getCurrentFragment(@IdRes containerId: Int): Fragment? {
  return supportFragmentManager.findFragmentById(containerId)
}

private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
  beginTransaction().apply {
    action()
  }.commit()
}

inline fun <reified T> AppCompatActivity.checkFragmentClass(
  @IdRes containerId: Int,
  isClass: (T) -> Unit,
  isNotClass: () -> Unit
) {
  val fragment = getCurrentFragment(containerId)
  if (fragment is T) {
    isClass(fragment)
  } else {
    isNotClass()
  }
}