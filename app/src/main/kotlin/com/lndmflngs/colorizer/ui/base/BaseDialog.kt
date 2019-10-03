package com.lndmflngs.colorizer.ui.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.lndmflngs.colorizer.data.DataManagerHelper

/**
 * Интерфейс для [BaseDialog]
 */
interface BaseDialogCallback {

  /** Показать диалоговое окно */
  fun show(fragmentManager: FragmentManager)

  /** Скрыть диалоговое окно */
  fun dismissDialog()
}

/**
 * ViewModel-суперкласс для упрощенного использования mvvm
 *
 * Используется для насследования другими ViewModel
 *
 * @param dataManager для обмена данными в приложении
 * @constructor Создание новой модели
 */
abstract class BaseDialogViewModel<N : BaseDialogCallback>(
  dataManager: DataManagerHelper
) : BaseViewModel<N>(dataManager) {

  /** Скрыть диалоговое окно */
  fun closeDialog() {
    navigator?.dismissDialog()
  }
}

/**
 * Dialog-суперкласс с внедрением зависимостей
 *
 * Кастомный dialog используется для насследования другими с указанием переменных для DI
 *
 * @param T тип ViewDataBinding для отображения полученного layout и его связывание
 * @param V тип [BaseViewModel] для работы с данными фрагмента
 * @constructor Создание нового dialog
 */
abstract class BaseDialog<T : ViewDataBinding, V : BaseDialogViewModel<*>> : DialogFragment(),
  BaseDialogCallback {

  /**
   * Переменная для отображения и дальнейшего связывания полученного layout
   */
  lateinit var viewDataBinding: T

  /**
   * Resource Id для разметки
   */
  @get:LayoutRes
  abstract val layoutId: Int

  /**
   * Переменнная ViewModel
   */
  abstract val viewModel: V

  /**
   * Создание кастомного dialog
   * @return базовое диалоговое окно
   */
  @NonNull
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val root = RelativeLayout(activity)
    root.layoutParams = ViewGroup.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.MATCH_PARENT
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

  /**
   * Показать диалоговое окно
   *
   * @param fragmentManager передаваемый FragmentContainer
   * @param tag создаваемого диалога
   */
  override fun show(
    fragmentManager: FragmentManager,
    tag: String?
  ) {
    val transaction = fragmentManager.beginTransaction()
    val prevFragment = fragmentManager.findFragmentByTag(tag)
    prevFragment?.let { transaction.remove(it) }
    transaction.addToBackStack(null)
    show(transaction, tag)
  }

  override fun dismissDialog() {
    dismiss()
  }

}