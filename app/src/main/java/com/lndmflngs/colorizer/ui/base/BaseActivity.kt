package com.lndmflngs.colorizer.ui.base

import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.lndmflngs.colorizer.extensions.transact
import dagger.android.AndroidInjection

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>> : AppCompatActivity(),
  BaseFragment.Callback {

  lateinit var viewDataBinding: T
    private set

  abstract val bindingVariable: Int

  @get:LayoutRes
  abstract val layoutId: Int

  @get:IdRes
  abstract val containerLayoutId: Int

  abstract val viewModel: V

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    window.setBackgroundDrawableResource(android.R.drawable.screen_background_light)
    super.onCreate(savedInstanceState)
    performDataBinding()
  }

  override fun onFragmentAttached() = Unit

  override fun onFragmentDetached(tag: String) = Unit

  fun replaceFragment(fragment: Fragment, tag: String) =
    supportFragmentManager.transact { replace(containerLayoutId, fragment, tag) }

  @TargetApi(Build.VERSION_CODES.M)
  fun hasPermission(permission: String): Boolean {
    return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
  }

  @TargetApi(Build.VERSION_CODES.M)
  fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      requestPermissions(permissions, requestCode)
    }
  }

  private fun performDataBinding() {
    viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
    viewDataBinding.apply {
      setVariable(bindingVariable, viewModel)
      executePendingBindings()
    }
  }

}