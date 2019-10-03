package com.lndmflngs.colorizer.ui.base

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>> : AppCompatActivity(),
    HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    /**
     * Переменная для отображения и дальнейшего связывания полученного layout
     * @see BaseActivity.performDataBinding
     */
    lateinit var viewDataBinding: T
        private set

    /**
     * Переменная для связывания
     * @see BaseActivity.performDataBinding
     */
    abstract val bindingVariable: Int

    /**
     * Resource Id для разметки
     * @see BaseActivity.performDataBinding
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * Переменнная ViewModel
     * @see BaseActivity.performDataBinding
     */
    abstract val viewModel: V

    /**
     * Выполнение кода после внедрения зависимостей, до создания ctx
     * @see BaseActivity.onCreate
     */
    open fun onPreCreate(): Unit? = null

    /**
     * Создание ctx с внедрением зависимостей и связыванием данных
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        window.setBackgroundDrawableResource(android.R.drawable.screen_background_light)
        super.onCreate(savedInstanceState)
        performDataBinding()
    }

    /**
     * Проверка на наличие разрешения
     *
     * @param permission запрашиваемое разрешение
     * @return true или false
     */
    fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissionRationale(
        permission: String,
        showReasonToRequest: () -> Unit
    ) {
        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            permission
        )
        showReasonToRequest()
    }

    /**
     * Запрос на разрешения
     *
     * @param permissions запрашиваемые разрешения
     * @return true или false
     */
    fun requestPermissionSafely(
        permissions: Array<String>,
        requestCode: Int
    ) {
        ActivityCompat.requestPermissions(
            this,
            permissions,
            requestCode
        )
    }

    /**
     * Выполнение связывния данных через [viewDataBinding],[layoutId], [bindingVariable], [viewModel]
     */
    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        viewDataBinding.apply {
            setVariable(bindingVariable, viewModel)
            executePendingBindings()
        }
    }

}