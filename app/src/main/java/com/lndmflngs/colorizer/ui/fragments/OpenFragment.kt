package com.lndmflngs.colorizer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lndmflngs.colorizer.R

class OpenFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_open, container, false)
        setHasOptionsMenu(true)
        return rootView
    }

    companion object {
        fun newInstance(): OpenFragment {
            return OpenFragment()
        }
    }
}