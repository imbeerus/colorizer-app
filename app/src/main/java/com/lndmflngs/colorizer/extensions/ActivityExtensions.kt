package com.lndmflngs.colorizer.extensions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

val pickPhoto = Intent(Intent.ACTION_GET_CONTENT).apply {
  addCategory(Intent.CATEGORY_OPENABLE)
  type = "image/*"
}

fun AppCompatActivity.replaceFragment(
  containerId: Int,
  fragment: Fragment
) {
  supportFragmentManager
    .beginTransaction()
    .replace(containerId, fragment)
    .commit()
}

fun AppCompatActivity.getCurrentFragment(
  containerId: Int
): Fragment? {
  return supportFragmentManager.findFragmentById(containerId)
}

inline fun <reified T> AppCompatActivity.checkCurrentFragment(
  containerId: Int,
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