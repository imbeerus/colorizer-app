package com.lndmflngs.colorizer.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel<*>> : Fragment() {

  private lateinit var rootView: View

  var baseActivity: BaseActivity<*, *>? = null
    private set

  lateinit var viewDataBinding: T
    private set

  abstract val bindingVariable: Int

  abstract val hasOptionMenu: Boolean

  @get:LayoutRes
  abstract val layoutId: Int

  @get:LayoutRes
  abstract val menuId: Int?

  abstract val viewModel: V

  override fun onAttach(context: Context) {
    super.onAttach(context)
    if (context is BaseActivity<*, *>) {
      this@BaseFragment.baseActivity = context
      context.onFragmentAttached()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidSupportInjection.inject(this)
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(hasOptionMenu)
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    menuId?.let { inflater.inflate(it, menu) }
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onCreateView(
    @NonNull inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
    rootView = viewDataBinding.root
    return rootView
  }

  override fun onDetach() {
    baseActivity = null
    super.onDetach()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    performDataBinding()
  }

  private fun performDataBinding() = with(viewDataBinding) {
    setVariable(bindingVariable, viewModel)
    executePendingBindings()
  }

  interface Callback {

    fun onFragmentAttached()

    fun onFragmentDetached(tag: String)
  }
}