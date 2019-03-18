package com.lndmflngs.colorizer.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun AppCompatActivity.replaceFragment(containerId: Int, fragment: Fragment) {
  supportFragmentManager.transact { replace(containerId, fragment) }
}

fun AppCompatActivity.getCurrentFragment(containerId: Int): Fragment? {
  return supportFragmentManager.findFragmentById(containerId)
}

fun AppCompatActivity.getCurrentFragmentByTag(tag: String): Fragment? {
  return supportFragmentManager.findFragmentByTag(tag)
}

private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
  beginTransaction().apply {
    action()
  }.commit()
}