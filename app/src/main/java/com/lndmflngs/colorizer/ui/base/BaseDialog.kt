package com.lndmflngs.colorizer.ui.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

abstract class BaseDialog<T : ViewDataBinding, V : BaseViewModel<*>> : DialogFragment() {

  var baseActivity: BaseActivity<*, *>? = null
    private set

  lateinit var viewDataBinding: T

  abstract val bindingVariable: Int

  @get:LayoutRes
  abstract val layoutId: Int

  abstract val viewModel: V

  override fun onAttach(context: Context) {
    super.onAttach(context)
    if (context is BaseActivity<*, *>) {
      this.baseActivity = context
      context.onFragmentAttached()
    }
  }

  @NonNull
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val root = RelativeLayout(activity)
    root.layoutParams = ViewGroup.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.WRAP_CONTENT
    )

    val dialog = Dialog(context!!).apply {
      requestWindowFeature(Window.FEATURE_NO_TITLE)
      setContentView(root)
    }
    dialog.window?.let {
      it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
      it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
    dialog.setCanceledOnTouchOutside(false)
    return dialog
  }

  override fun onDetach() {
    baseActivity = null
    super.onDetach()
  }

  override fun show(fragmentManager: FragmentManager, tag: String?) {
    val transaction = fragmentManager.beginTransaction()
    val prevFragment = fragmentManager.findFragmentByTag(tag)
    prevFragment?.let { transaction.remove(it) }
    transaction.addToBackStack(null)
    show(transaction, tag)
  }

  fun dismissDialog(tag: String) {
    dismiss()
    baseActivity?.onFragmentDetached(tag)
  }
}