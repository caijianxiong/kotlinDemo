package com.cjx.kotlin.base

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.cjx.kotlin.base.net.DataState
import com.cjx.kotlin.base.vm.AppViewModelFactory
import com.cjx.kotlin.base.vm.BaseAndroidViewModel
import com.cjx.kotlin.base.vm.BaseViewModel
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VM : BaseViewModel<*>, VB : ViewBinding>(
    private val inflate: (LayoutInflater) -> VB
) : AppCompatActivity(), IBaseView, ViewModelStoreOwner {

    private var permissionCallback: PermissionCallback? = null
    private val PERMISSION_REQUEST_CODE = 1001

    protected lateinit var viewModel: VM
    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        
        viewModel.loadingDataState.observe(this) {
            when (it.state) {
                DataState.STATE_LOADING -> showLoading(it.msg)
                else -> dismissLoading()
            }
        }
        
        initView()
        setupListener()
        onActivityCreated(savedInstanceState)
        initObservable()
    }

    private fun initViewModel() {
        // ViewModel creation via reflection
        // To further decouple, consider passing a ViewModelFactory or using Hilt
        val vmClass = getActualTypeClass<VM>(0)
        viewModel = if (BaseAndroidViewModel::class.java.isAssignableFrom(vmClass)) {
            ViewModelProvider(this, AppViewModelFactory(application))[vmClass]
        } else {
            ViewModelProvider(this)[vmClass]
        }
    }

    abstract fun onActivityCreated(savedInstanceState: Bundle?)

    abstract fun setupListener()

    open fun showLoading(msg: String? = null) {
        // Implement loading dialog here
    }

    open fun dismissLoading() {
        // Dismiss loading dialog here
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getActualTypeClass(index: Int): Class<T> {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[index]
        return type as Class<T>
    }

    interface PermissionCallback {
        fun onPermissionGranted()
        fun onPermissionDenied(deniedPermissions: List<String>)
        fun onPermissionDeniedPermanently(deniedPermissions: List<String>)
    }

    fun requestPermissions(permissions: Array<String>, callback: PermissionCallback) {
        this.permissionCallback = callback
        val needRequestPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (needRequestPermissions.isEmpty()) {
            callback.onPermissionGranted()
            return
        }

        ActivityCompat.requestPermissions(
            this,
            needRequestPermissions.toTypedArray(),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != PERMISSION_REQUEST_CODE) return

        val deniedPermissions = mutableListOf<String>()
        val permanentlyDeniedPermissions = mutableListOf<String>()

        for (i in permissions.indices) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i])
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                    permanentlyDeniedPermissions.add(permissions[i])
                }
            }
        }

        when {
            deniedPermissions.isEmpty() -> permissionCallback?.onPermissionGranted()
            permanentlyDeniedPermissions.isNotEmpty() -> permissionCallback?.onPermissionDeniedPermanently(permanentlyDeniedPermissions)
            else -> permissionCallback?.onPermissionDenied(deniedPermissions)
        }
        permissionCallback = null
    }
}