package com.lndmflngs.colorizer.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lndmflngs.colorizer.R
import kotlinx.android.synthetic.main.fragment_result.*

class ResultFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageToShow = arguments?.getString(ARGUMENT_IMG)!!
        resultView.setImageURI(Uri.parse(imageToShow))
    }

    companion object {
        const val ARGUMENT_IMG = "ResultFragment:img"

        fun newInstance(image: String): ResultFragment {
            val fragment = ResultFragment()
            val args = Bundle()
            args.putString(ARGUMENT_IMG, image)
            fragment.arguments = args
            return fragment
        }
    }
}