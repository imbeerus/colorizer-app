package com.lndmflngs.colorizer.ui.open

import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.ViewModelProviderFactory
import com.lndmflngs.colorizer.databinding.FragmentOpenBinding
import com.lndmflngs.colorizer.extensions.getViewModel
import com.lndmflngs.colorizer.ui.base.BaseFragment
import com.lndmflngs.colorizer.ui.main.MainActivity
import javax.inject.Inject

class OpenFragment : BaseFragment<FragmentOpenBinding, OpenViewModel>(), OpenNavigator {

  @Inject
  lateinit var factory: ViewModelProviderFactory

  override val bindingVariable: Int = BR.viewModel
  override val layoutId: Int = R.layout.fragment_open
  override val viewModel: OpenViewModel by lazy { getViewModel<OpenViewModel>(factory) }

  override val hasOptionMenu: Boolean = false
  override val menuId: Int? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.navigator = this
  }

  override fun startPickImage() = with(activity) {
    val pickPhoto = android.content.Intent(android.content.Intent.ACTION_GET_CONTENT).apply {
      addCategory(android.content.Intent.CATEGORY_OPENABLE)
      type = "image/*"
    }
    // android.app.SuperNotCalledException: Activity {android/com.android.internal.app.ChooserActivity} did not call through to super.onStop()
    // just an emulator issue (http://qaru.site/questions/12400777/sharecompat-intentbuilder-crashing-every-time-on-android-4)
    startActivityForResult(
      android.content.Intent.createChooser(
        pickPhoto,
        getString(R.string.title_select_picture)
      ), MainActivity.REQUEST_TAKE_IMAGE
    )
  }

  companion object {
    const val TAG = "OpenFragment"

    fun newInstance(): OpenFragment {
      return OpenFragment()
    }
  }
}