package com.cjx.kotlin.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.cjx.kotlin.base.vm.AppViewModelFactory
import com.cjx.kotlin.base.vm.BaseAndroidViewModel
import com.cjx.kotlin.base.vm.BaseViewModel
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VM : BaseViewModel<*>, VB : ViewBinding> : RxAppCompatActivity(),
    IBaseView {


    protected val viewModel by lazy {
        createViewModel()
    }

    protected val binding by lazy {
        createViewBinding()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.loadingDataState.observe(this) {
            when (it.state) {
                DataState.STATE_LOADING ->
                    showLoading(it.msg)

                else ->
                    dismissLoading()
            }
        }
        initParam()
        onActivityCreated(savedInstanceState)
    }

    /**
     * Activity content view created.
     *  @param  savedInstanceState savedInstanceState
     */
    abstract fun onActivityCreated(savedInstanceState: Bundle?)

    /**
     * 显示Loading
     */
    open fun showLoading(msg: String? = null) {
//        ToastUtils.showToast("showLoading")
    }

    /**
     * 隐藏Loading
     */
    open fun dismissLoading() {
//        ToastUtils.showToast("hideLoading")
    }

    /**
     * Create ViewBinding
     */
    @Suppress("UNCHECKED_CAST")
    open fun createViewBinding(): VB {
        val actualGenericsClass = findActualGenericsClass<VB>(ViewBinding::class.java)
            ?: throw NullPointerException("Can not find a ViewBinding Generics in ${javaClass.simpleName}")
        try {
            val inflate =
                actualGenericsClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
            return inflate.invoke(null, layoutInflater) as VB
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        throw RuntimeException("don't find class type! ")
    }

    /**
     * Create ViewModel
     *  @return  ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    open fun createViewModel(): VM {
        val actualGenericsClass = findActualGenericsClass<VM>(BaseViewModel::class.java)
            ?: throw NullPointerException("Can not find a ViewModel Generics in ${javaClass.simpleName}")
        if (Modifier.isAbstract(actualGenericsClass.modifiers)) {
            throw IllegalStateException("$actualGenericsClass is an abstract class,abstract ViewModel class can not create a instance!")
        }
        // 判断如果是 BaseAndroidViewModel，则使用 AppViewModelFactory 来生成
        if (BaseAndroidViewModel::class.java.isAssignableFrom(actualGenericsClass)) {
            return ViewModelProvider(this, AppViewModelFactory(application))[actualGenericsClass]
        }
        return ViewModelProvider(this)[actualGenericsClass]
    }

    internal fun <T> Any.findActualGenericsClass(cls: Class<*>): Class<T>? {
        val genericSuperclass = javaClass.genericSuperclass
        if (genericSuperclass !is ParameterizedType) {
            return null
        }
        // 获取类的所有泛型参数数组
        val actualTypeArguments = genericSuperclass.actualTypeArguments
        // 遍历泛型数组
        actualTypeArguments.forEach {
            if (it is Class<*> && cls.isAssignableFrom(it)) {
                return it as Class<T>
            } else if (it is ParameterizedType) {
                val rawType = it.rawType
                if (rawType is Class<*> && cls.isAssignableFrom(rawType)) {
                    return rawType as Class<T>
                }
            }
        }
        return null
    }


}