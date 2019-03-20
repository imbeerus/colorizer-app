package com.lndmflngs.colorizer.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.ViewModelProviderFactory
import com.lndmflngs.colorizer.databinding.ActivityMainBinding
import com.lndmflngs.colorizer.extensions.getViewModel
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
        showOpenFragment()
      }
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == REQUEST_TAKE_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {
      val bitmap = viewModel.dataManager.getMediaBitmap(data?.data!!)
      viewModel.handleImage(bitmap)
    }
  }

  override fun onBackPressed() {
    val currentFragment = supportFragmentManager.findFragmentById(containerLayoutId)
    if (currentFragment == null || currentFragment.tag == OpenFragment.TAG) {
      super.onBackPressed()
    } else {
      onFragmentDetached(ResultFragment.TAG)
    }
  }

  override fun onFragmentDetached(tag: String) {
    super.onFragmentDetached(tag)
    val fragment = supportFragmentManager.findFragmentByTag(tag)
    if (fragment != null) {
      supportFragmentManager
        .beginTransaction()
        .disallowAddToBackStack()
        .remove(fragment)
        .commitNow()
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

  override fun showOpenFragment() {
    supportFragmentManager
      .beginTransaction()
      .disallowAddToBackStack()
      .add(containerLayoutId, OpenFragment.newInstance(), OpenFragment.TAG)
      .commit()
  }

  override fun showResultFragment() {
    val byteArray = viewModel.imageData.value
    if (byteArray != null) {
      supportFragmentManager
        .beginTransaction()
        .disallowAddToBackStack()
        .add(containerLayoutId, ResultFragment.newInstance(byteArray), ResultFragment.TAG)
        .commit()
    }
    onFragmentDetached(OpenFragment.TAG)
  }

  override fun handleError(throwable: Throwable) {
    Log.e(TAG, throwable.message)
  }

  private fun setupLayout() {
    val toolbar = viewDataBinding.appbar.toolbar
    viewModel.title.set(getString(R.string.app_name))
    setSupportActionBar(toolbar)
  }

  companion object {
    private const val TAG = "MainActivity"

    const val REQUEST_TAKE_IMAGE = 0
  }
}
