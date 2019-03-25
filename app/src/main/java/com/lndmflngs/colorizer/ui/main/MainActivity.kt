package com.lndmflngs.colorizer.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.ViewModelProviderFactory
import com.lndmflngs.colorizer.databinding.ActivityMainBinding
import com.lndmflngs.colorizer.extensions.getViewModel
import com.lndmflngs.colorizer.ui.about.AboutDialog
import com.lndmflngs.colorizer.ui.base.BaseActivity
import com.lndmflngs.colorizer.ui.open.OpenFragment
import com.lndmflngs.colorizer.ui.result.ResultFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(),
  MainNavigator, HasSupportFragmentInjector {

  @Inject
  lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

  @Inject
  lateinit var factory: ViewModelProviderFactory

  override val bindingVariable: Int = BR.viewModel
  override val layoutId: Int = R.layout.activity_main
  override val containerLayoutId: Int = R.id.fragment_container
  override val viewModel: MainViewModel by lazy { getViewModel<MainViewModel>(factory) }

  override fun supportFragmentInjector(): AndroidInjector<Fragment> {
    return fragmentDispatchingAndroidInjector
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.navigator = this@MainActivity
    setupLayout()
    handleIntent {
      if (savedInstanceState == null) {
        replaceFragment(OpenFragment.newInstance(), OpenFragment.TAG)
      }
    }
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

  override fun handleIntent(homeScreenStart: () -> Unit) = with(intent) {
    when (action) {
      Intent.ACTION_SEND -> {
        if (type!!.startsWith("image/")) {
          viewModel.handleImageSend(this)
        }
      }
      else -> homeScreenStart()
    }
  }

  override fun showResultFragment() {
    replaceFragment(ResultFragment.newInstance(viewModel.imageToColorize), ResultFragment.TAG)
  }

  override fun handleError(throwable: Throwable) {
    Log.e(TAG, throwable.message)
  }

  private fun setupLayout() {
    viewModel.title.set(getString(R.string.app_name))
    viewDataBinding.appbar.toolbar.apply {
      setSupportActionBar(this)
      setTitleTextAppearance(this@MainActivity, R.style.TextAppearance_RobotoMedium)
    }
  }

  companion object {
    private const val TAG = "MainActivity"

    const val REQUEST_TAKE_IMAGE = 0
  }
}
