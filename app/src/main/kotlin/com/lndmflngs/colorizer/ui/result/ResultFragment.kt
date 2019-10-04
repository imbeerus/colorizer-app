package com.lndmflngs.colorizer.ui.result

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.lndmflngs.colorizer.BR
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.ViewModelProviderFactory
import com.lndmflngs.colorizer.databinding.FragmentResultBinding
import com.lndmflngs.colorizer.extensions.getViewModel
import com.lndmflngs.colorizer.extensions.lockOrientation
import com.lndmflngs.colorizer.extensions.startPickImage
import com.lndmflngs.colorizer.extensions.unlockOrientation
import com.lndmflngs.colorizer.ui.base.BaseFragment
import javax.inject.Inject

class ResultFragment : BaseFragment<FragmentResultBinding, ResultViewModel>(), ResultNavigator {

    @Inject
    lateinit var factory: ViewModelProviderFactory

    override val bindingVariable: Int = BR.viewModel
    override val layoutId: Int = R.layout.fragment_result
    override val viewModel: ResultViewModel by lazy { getViewModel<ResultViewModel>(factory) }

    override val hasOptionMenu: Boolean = true

    private val menuItems = arrayOf(R.id.change, R.id.share)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
        viewModel.isLoadingLiveData.observe(this, Observer<Boolean> { isLoading ->
            if (isLoading) {
                (activity as AppCompatActivity).lockOrientation()
            } else {
                (activity as AppCompatActivity).unlockOrientation()
            }
        })
    }

    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater
    ) {
        inflater.inflate(R.menu.menu_result, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menuItems.forEach { menu.findItem(it).isEnabled = viewModel.isMenuActionsEnabled.get() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.change -> {
                (activity as AppCompatActivity).startPickImage()
                true
            }
            R.id.share -> {
                viewModel.shareImage(viewDataBinding.resultImageView)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun startShareImage(uri: Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/*"
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.title_share_via)))
    }

    override fun showResult() {
        activity?.invalidateOptionsMenu() // update menu states
    }

    override fun handleError(throwable: Throwable) {
        Log.d(TAG, throwable.message)
    }

    companion object {
        const val TAG = "ResultFragment"

        fun newInstance(): ResultFragment {
            return ResultFragment()
        }
    }

}