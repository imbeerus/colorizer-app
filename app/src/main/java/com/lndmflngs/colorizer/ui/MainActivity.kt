package com.lndmflngs.colorizer.ui

import android.os.Bundle
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.extensions.replaceFragment
import com.lndmflngs.colorizer.ui.fragments.OpenFragment

class MainActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    handleIntent {
      if (savedInstanceState == null) {
        replaceFragment(R.id.fragment_container, OpenFragment.newInstance())
      }
    }
  }
}
