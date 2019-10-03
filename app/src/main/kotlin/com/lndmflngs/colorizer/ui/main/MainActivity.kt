package com.lndmflngs.colorizer.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.library.baseAdapters.BR
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.ViewModelProviderFactory
import com.lndmflngs.colorizer.databinding.ActivityMainBinding
import com.lndmflngs.colorizer.extensions.getViewModel
import com.lndmflngs.colorizer.extensions.replaceFragment
import com.lndmflngs.colorizer.extensions.setupActionBar
import com.lndmflngs.colorizer.ui.about.AboutDialog
import com.lndmflngs.colorizer.ui.base.BaseFragmentActivity
import com.lndmflngs.colorizer.ui.open.OpenFragment
import com.lndmflngs.colorizer.ui.result.ResultFragment
import javax.inject.Inject

class MainActivity : BaseFragmentActivity<ActivityMainBinding, MainViewModel>(), MainNavigator {

    @Inject
    lateinit var factory: ViewModelProviderFactory

    override val bindingVariable: Int = BR.viewModel
    override val layoutId: Int = R.layout.activity_main
    override val viewModel: MainViewModel by lazy { getViewModel<MainViewModel>(factory) }

    override val containerId: Int = R.id.fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this@MainActivity
        setupActionBar(viewDataBinding.appbar.toolbar) {
            viewModel.title.set(getString(R.string.app_name))
        }
        handleImageIntent(defaultStart = {
            if (savedInstanceState == null) {
                replaceFragment(containerId, OpenFragment.newInstance(), OpenFragment.TAG)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.about_app -> {
                AboutDialog.newInstance().show(supportFragmentManager)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {
            val bitmap = viewModel.dataManager.getMediaBitmap(data?.data!!)
            viewModel.handleImage(bitmap)
        }
    }

    override fun handleImageIntent(defaultStart: () -> Unit) = with(intent) {
        when (action) {
            Intent.ACTION_SEND -> {
                if (type!!.startsWith("image/")) {
                    viewModel.handleImageSend(this)
                }
            }
            else -> defaultStart()
        }
    }

    override fun showResultFragment() {
        replaceFragment(
            containerId,
            ResultFragment.newInstance(viewModel.imageToColorize),
            ResultFragment.TAG
        )
    }

    override fun handleError(throwable: Throwable) {
        Log.e(TAG, throwable.message)
    }

    companion object {
        private const val TAG = "MainActivity"

        const val REQUEST_TAKE_IMAGE = 0
    }
}
