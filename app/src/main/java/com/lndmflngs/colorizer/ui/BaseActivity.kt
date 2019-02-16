package com.lndmflngs.colorizer.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.extensions.replaceFragment
import com.lndmflngs.colorizer.extensions.toast
import com.lndmflngs.colorizer.ui.fragments.ResultFragment
import kotlinx.android.synthetic.main.include_toolbar.*

abstract class BaseActivity : AppCompatActivity() {

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initToolbar()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                val selectedImage = data?.data.toString()
                replaceFragment(R.id.fragment_container, ResultFragment.newInstance(selectedImage))
            } else {
                toast("Error during to pick photo")
            }
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_leaves)
        }
    }

    companion object {
        const val REQUEST_TAKE_IMAGE = 0
        const val CLIENT_COLORFUL_IMAGE_COLORIZATION = "deeplearning/ColorfulImageColorization/1.1.13"
    }
}