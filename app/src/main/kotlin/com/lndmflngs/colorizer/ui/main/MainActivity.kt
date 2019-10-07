package com.lndmflngs.colorizer.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.lndmflngs.colorizer.AboutDialog
import com.lndmflngs.colorizer.BR
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.ViewModelProviderFactory
import com.lndmflngs.colorizer.databinding.ActivityMainBinding
import com.lndmflngs.colorizer.extensions.getViewModel
import com.lndmflngs.colorizer.extensions.replaceFragment
import com.lndmflngs.colorizer.extensions.setupActionBar
import com.lndmflngs.colorizer.extensions.toast
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
        checkWritePermission()
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

    override fun checkWritePermission() {
        val writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        if (!hasPermission(writePermission)) {
            requestPermissionSafely(
                arrayOf(writePermission),
                PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == INTENT_REQUEST_TAKE_IMAGE && resultCode == RESULT_OK) {
            val bitmap = viewModel.dataManager.getMediaBitmap(data?.data!!)
            viewModel.handleImage(bitmap)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    toast(getString(R.string.permission_received))
                }
                return
            }
            else -> {
            }
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
        replaceFragment(containerId, ResultFragment.newInstance(), ResultFragment.TAG)
    }

    override fun handleError(throwable: Throwable) {
        Log.e(TAG, throwable.message!!)
    }

    companion object {
        private const val TAG = "MainActivity"

        const val INTENT_REQUEST_TAKE_IMAGE = 0
        const val PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 124
    }
}
