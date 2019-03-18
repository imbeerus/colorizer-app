package com.lndmflngs.colorizer.ui.main

import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.ViewModelProviderFactory
import com.lndmflngs.colorizer.databinding.ActivityMainBinding
import com.lndmflngs.colorizer.extensions.getViewModel
import com.lndmflngs.colorizer.extensions.replaceFragment
import com.lndmflngs.colorizer.extensions.toast
import com.lndmflngs.colorizer.ui.base.BaseActivity
import com.lndmflngs.colorizer.ui.main.open.OpenFragment
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
    replaceFragment(containerLayoutId, OpenFragment.newInstance())
    // TODO: fetch data?
  }

  override fun showOpenFragment() {
    // TODO: "not implemented"
  }

  override fun showResultFragment() {
    // TODO: "not implemented"
  }

  override fun handleError(throwable: Throwable) {
    toast(throwable.localizedMessage)
    throwable.printStackTrace()
  }

  private fun setupLayout() {
    supportActionBar?.apply {
      setDisplayShowTitleEnabled(false)
    }
  }

  companion object {
    private const val TAG = "MainActivity"
  }
}
