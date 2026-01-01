package com.cjx.kotlin.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cjx.kotlin.base.vm.AppViewModelFactory
import com.cjx.kotlin.base.vm.BaseAndroidViewModel
import com.cjx.kotlin.base.vm.BaseViewModel
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<VM : BaseViewModel<*>, VB : ViewDataBinding>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment(), IBaseView {

    private var _binding: VB? = null
    // This property is only valid between onCreateView and onDestroyView.
    protected val binding get() = _binding!!
    
    open var viewModel: VM? = null
    open var viewModelId = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewDataBinding()
        initView()
        initObservable()
        initData()
    }

    private fun initViewDataBinding() {
        viewModelId = initVariableId()
        val vmClass = getViewModelClass()
        viewModel = if (BaseAndroidViewModel::class.java.isAssignableFrom(vmClass)) {
            ViewModelProvider(this, AppViewModelFactory(requireActivity().application))[vmClass]
        } else {
            ViewModelProvider(this)[vmClass]
        }
        
        binding.setVariable(viewModelId, viewModel)
        binding.lifecycleOwner = viewLifecycleOwner
        
        lifecycle.addObserver(viewModel!!)
    }
    
    @Suppress("UNCHECKED_CAST")
    private fun getViewModelClass(): Class<VM> {
        val type = javaClass.genericSuperclass
        return if (type is ParameterizedType) {
            type.actualTypeArguments[0] as Class<VM>
        } else {
            throw IllegalStateException("BaseFragment must specify generic type for VM")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun initData()
    abstract fun initVariableId(): Int
}