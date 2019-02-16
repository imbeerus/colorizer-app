package com.lndmflngs.colorizer.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.replaceFragment(containerId: Int, fragment: Fragment) {
    supportFragmentManager
        .beginTransaction()
        .replace(containerId, fragment)
        .commit()
}