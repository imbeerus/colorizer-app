package com.lndmflngs.colorizer.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.lndmflngs.colorizer.BuildConfig
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.ViewModelProviderFactory
import com.lndmflngs.colorizer.databinding.DialogAboutBinding
import com.lndmflngs.colorizer.extensions.getViewModel
import com.lndmflngs.colorizer.ui.base.BaseDialog
import com.lndmflngs.colorizer.ui.base.BaseFragmentActivity
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class AboutDialog : BaseDialog<DialogAboutBinding, AboutViewModel>(), AboutCallback {

    @Inject
    lateinit var factory: ViewModelProviderFactory

    override val layoutId: Int = R.layout.dialog_about
    override val viewModel: AboutViewModel by lazy { getViewModel<AboutViewModel>(factory) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // perform dataBinding
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        AndroidSupportInjection.inject(this) // performDependencyInjection
        viewDataBinding.viewModel = viewModel
        viewModel.navigator = this
        viewModel.appVersion.set("${getString(R.string.title_version)} ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
        return viewDataBinding.root
    }

    override fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    override fun onDetach() {
        (context as BaseFragmentActivity<*, *>).onFragmentDetached(TAG)
        super.onDetach()
    }

    companion object {

        val TAG: String = AboutDialog::class.java.simpleName

        fun newInstance(): AboutDialog {
            return AboutDialog()
        }
    }

}