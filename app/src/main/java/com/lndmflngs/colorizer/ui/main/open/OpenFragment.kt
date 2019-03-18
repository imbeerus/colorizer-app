package com.lndmflngs.colorizer.ui.main.open

import androidx.databinding.library.baseAdapters.BR
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.ViewModelProviderFactory
import com.lndmflngs.colorizer.databinding.FragmentOpenBinding
import com.lndmflngs.colorizer.extensions.getViewModel
import com.lndmflngs.colorizer.ui.base.BaseFragment
import javax.inject.Inject

class OpenFragment : BaseFragment<FragmentOpenBinding, OpenViewModel>(), OpenNavigator {

  @Inject
  lateinit var factory: ViewModelProviderFactory

  override val bindingVariable: Int = BR.viewModel
  override val layoutId: Int = R.layout.fragment_open
  override val viewModel: OpenViewModel by lazy { getViewModel<OpenViewModel>(factory) }

  override val hasOptionMenu: Boolean = false
  override val menuId: Int? = null

  override fun handleError(throwable: Throwable) {
    // TODO"not implemented"
  }

  companion object {
    private const val TAG = "OpenFragment"

    fun newInstance(): OpenFragment {
      return OpenFragment()
    }
  }
}