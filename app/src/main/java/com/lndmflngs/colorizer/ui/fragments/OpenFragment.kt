package com.lndmflngs.colorizer.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.ui.BaseActivity

class OpenFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val rootView = inflater.inflate(R.layout.fragment_open, container, false)
    rootView.setOnClickListener {
      val pickPhoto = Intent(Intent.ACTION_GET_CONTENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "image/*"
      }
      activity?.startActivityForResult(
        Intent.createChooser(pickPhoto, getString(R.string.title_select_picture)),
        BaseActivity.REQUEST_TAKE_IMAGE
      )
    }
    return rootView
  }

  companion object {
    fun newInstance(): OpenFragment {
      return OpenFragment()
    }
  }
}