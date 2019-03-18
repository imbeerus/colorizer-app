package com.lndmflngs.colorizer.ui.main.result

import androidx.databinding.library.baseAdapters.BR
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.ViewModelProviderFactory
import com.lndmflngs.colorizer.databinding.FragmentOpenBinding
import com.lndmflngs.colorizer.extensions.getViewModel
import com.lndmflngs.colorizer.ui.base.BaseFragment
import javax.inject.Inject

class ResultFragment : BaseFragment<FragmentOpenBinding, ResultViewModel>(), ResultNavigator {

  @Inject
  lateinit var factory: ViewModelProviderFactory

  override val bindingVariable: Int = BR.viewModel
  override val layoutId: Int = R.layout.fragment_open
  override val viewModel: ResultViewModel by lazy { getViewModel<ResultViewModel>(factory) }

  override val hasOptionMenu: Boolean = false
  override val menuId: Int? = null

  override fun handleError(throwable: Throwable) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  companion object {
    private const val TAG = "ResultFragment"

    fun newInstance(): ResultFragment {
      return ResultFragment()
    }
  }
}