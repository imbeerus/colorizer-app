package com.lndmflngs.colorizer.ui.result

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.ViewModelProviderFactory
import com.lndmflngs.colorizer.databinding.FragmentOpenBinding
import com.lndmflngs.colorizer.extensions.getViewModel
import com.lndmflngs.colorizer.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_result.view.resultImageView
import javax.inject.Inject

class ResultFragment : BaseFragment<FragmentOpenBinding, ResultViewModel>(), ResultNavigator {

  @Inject
  lateinit var factory: ViewModelProviderFactory

  override val bindingVariable: Int = BR.viewModel
  override val layoutId: Int = R.layout.fragment_open
  override val viewModel: ResultViewModel by lazy { getViewModel<ResultViewModel>(factory) }

  override val hasOptionMenu: Boolean = true
  override val menuId: Int? = R.menu.menu_result

  private val menuItems = arrayOf(R.id.change, R.id.share)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.navigator = this
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.imageView = viewDataBinding.root.resultImageView
  }

  override fun onPrepareOptionsMenu(menu: Menu) {
    super.onPrepareOptionsMenu(menu)
    menuItems.forEach { menu.findItem(it).isEnabled = viewModel.isMenuActionsEnabled.get() }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.change -> {
        startPickImage()
        true
      }
      R.id.share -> {
        viewModel.shareImage()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun startPickImage() {
//    activity?.pickImage()
  }

  override fun startShareImage(uri: Uri) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
      putExtra(Intent.EXTRA_STREAM, uri)
      type = "image/*"
    }
    startActivity(Intent.createChooser(shareIntent, getString(R.string.title_share_via)))
  }

  override fun updateMenu() {
    activity?.invalidateOptionsMenu()
  }

  override fun handleError(throwable: Throwable) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  companion object {
    const val TAG = "ResultFragment"

    private const val ARGUMENT_PICKED_IMG = "ResultFragment:img"

    fun newInstance(imgData: ByteArray): ResultFragment {
      val fragment = ResultFragment()
      val args = Bundle()
      args.putByteArray(ARGUMENT_PICKED_IMG, imgData)
      fragment.arguments = args
      return fragment
    }
  }

}