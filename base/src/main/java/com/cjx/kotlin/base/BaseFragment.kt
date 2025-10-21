package com.cjx.kotlin.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.cjx.kotlin.base.vm.AppViewModelFactory
import com.cjx.kotlin.base.vm.BaseAndroidViewModel
import com.cjx.kotlin.base.vm.BaseViewModel
import com.trello.rxlifecycle2.components.support.RxFragment
import java.lang.reflect.ParameterizedType


abstract class BaseFragment<VM : BaseViewModel<*>, VB : ViewDataBinding> : RxFragment(),
    IBaseView {

    open var binding: VB? = null
    open var viewModel: VM? = null
    open var viewModelId = 0


    @Deprecated("Deprecated in Java")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }


    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater,
            initContentView(inflater, container, savedInstanceState),
            container,
            false
        ) as VB?

        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding()
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initObservable()
        initData() // 初始化数据加载
    }


    private fun initViewDataBinding() {
        viewModelId = initVariableId()
        // 获取ViewModel的实际类型
        val vmClass = getViewModelClass()
        // 根据类型创建ViewModel（支持BaseAndroidViewModel）
        viewModel = if (BaseAndroidViewModel::class.java.isAssignableFrom(vmClass)) {
            ViewModelProvider(this, AppViewModelFactory(requireActivity().application))[vmClass]
        } else {
            ViewModelProvider(this)[vmClass]
        }
        // 绑定ViewModel到XML
        binding?.setVariable(viewModelId, viewModel)
        binding?.lifecycleOwner = this
        // 关联生命周期
        lifecycle.addObserver(viewModel!!)
        viewModel?.injectLifecycleProvider(this)
    }

    /**
     * 通过反射获取ViewModel的实际类型
     */
    @Suppress("UNCHECKED_CAST")
    private fun getViewModelClass(): Class<VM> {
        val type = javaClass.genericSuperclass
        return if (type is ParameterizedType) {
            type.actualTypeArguments[1] as Class<VM>
        } else {
            throw IllegalStateException("BaseFragment must specify generic type for VM")
        }
    }

    /**
     * 初始化数据（新增抽象方法，子类实现数据加载逻辑）
     */
    abstract fun initData()


    /**
     * 返回variableid
     */
    abstract fun initVariableId(): Int


    /**
     * 返回布局id
     */
    abstract fun initContentView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int

}