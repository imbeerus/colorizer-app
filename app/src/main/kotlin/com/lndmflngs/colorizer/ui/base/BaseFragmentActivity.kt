package com.lndmflngs.colorizer.ui.base

import androidx.databinding.ViewDataBinding

abstract class BaseFragmentActivity<T : ViewDataBinding, V : BaseViewModel<*>> :
    BaseActivity<T, V>(), BaseFragment.Callback {

    abstract val containerId: Int

    override fun onFragmentAttached() = Unit

    override fun onFragmentDetached(tag: String) = Unit
}