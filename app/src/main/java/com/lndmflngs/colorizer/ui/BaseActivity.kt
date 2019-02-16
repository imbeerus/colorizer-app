package com.lndmflngs.colorizer.ui

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.lndmflngs.colorizer.R
import kotlinx.android.synthetic.main.include_toolbar.*

abstract class BaseActivity : AppCompatActivity() {

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initToolbar()
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
        const val CLIENT_COLORFUL_IMAGE_COLORIZATION = "deeplearning/ColorfulImageColorization/1.1.13"
    }
}