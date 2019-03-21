package com.lndmflngs.colorizer.ui.result

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.library.baseAdapters.BR
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.ViewModelProviderFactory
import com.lndmflngs.colorizer.databinding.FragmentResultBinding
import com.lndmflngs.colorizer.extensions.getViewModel
import com.lndmflngs.colorizer.extensions.lockOrientation
import com.lndmflngs.colorizer.extensions.startPickImage
import com.lndmflngs.colorizer.extensions.unlockOrientation
import com.lndmflngs.colorizer.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_result.view.resultImageView
import javax.inject.Inject

class ResultFragment : BaseFragment<FragmentResultBinding, ResultViewModel>(), ResultNavigator {

  @Inject
  lateinit var factory: ViewModelProviderFactory

  override val bindingVariable: Int = BR.viewModel
  override val layoutId: Int = R.layout.fragment_result
  override val viewModel: ResultViewModel by lazy { getViewModel<ResultViewModel>(factory) }

  override val hasOptionMenu: Boolean = true
  override val menuId: Int? = R.menu.menu_result

  private val menuItems = arrayOf(R.id.change, R.id.share)

  private lateinit var imageView: ImageView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.navigator = this
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewDataBinding.root.resultImageView.apply {
      imageView = this
      viewModel.imageView = this
    }
    (activity as AppCompatActivity).lockOrientation()
//    if (savedInstanceState != null) {
//      resultImgSource = savedInstanceState.getString(BUNDLE_RESULT_IMG_SOURCE)!!
//      showColoredResult()
//    } else {
    val byteArray = arguments?.getByteArray(ARGUMENT_PICKED_IMG)!!
    viewModel.sendImageToColorize(byteArray)
//    }
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
    (activity as AppCompatActivity).startPickImage()
  }

  override fun startShareImage(uri: Uri) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
      putExtra(Intent.EXTRA_STREAM, uri)
      type = "image/*"
    }
    startActivity(Intent.createChooser(shareIntent, getString(R.string.title_share_via)))
  }

  override fun showResult() {
    (activity as AppCompatActivity).unlockOrientation()
    activity?.invalidateOptionsMenu() // update menu states
    viewModel.dataManager.loadImage(viewModel.resultImageSource, imageView)
  }

  override fun handleError(throwable: Throwable) {
    Log.d(TAG, throwable.message)
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