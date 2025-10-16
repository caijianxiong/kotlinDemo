package com.cjx.kotlin.base

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.cjx.kotlin.base.vm.AppViewModelFactory
import com.cjx.kotlin.base.vm.BaseAndroidViewModel
import com.cjx.kotlin.base.vm.BaseViewModel
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VM : BaseViewModel<*>, VB : ViewBinding> : RxAppCompatActivity(),
    IBaseView, ViewModelStoreOwner {
    private var permissionCallback: PermissionCallback? = null
    private val PERMISSION_REQUEST_CODE = 1001

    // ViewModel实例
    protected lateinit var viewModel: VM

    // 自动初始化的ViewBinding
    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1. 初始化ViewModel
        initViewModel()
        // 2. 自动创建ViewBinding
        initViewBinding()
        // 3. 设置布局
        setContentView(binding.root)
        viewModel.loadingDataState.observe(this) {
            when (it.state) {
                DataState.STATE_LOADING ->
                    showLoading(it.msg)

                else ->
                    dismissLoading()
            }
        }
        initView()
        setupListener()
        onActivityCreated(savedInstanceState)
        initObservable()
    }


    private fun initViewModel() {
        val vmClass = getActualTypeClass<VM>(0)
        viewModel = ViewModelProvider(this)[vmClass]
    }

    //    // 自动初始化ViewBinding（核心逻辑）
    private fun initViewBinding() {
        try {
            // 获取VB泛型的实际类型
            val vbClass = getActualTypeClass<VB>(1)
            // 反射获取inflate方法（支持多种参数组合）
            val inflateMethod = findInflateMethod(vbClass)
            // 调用inflate方法创建实例
            binding = invokeInflateMethod(inflateMethod)
        } catch (e: Exception) {
            throw RuntimeException("自动创建ViewBinding失败，请检查布局文件和泛型声明", e)
        }
    }

    /**
     * Activity content view created.
     *  @param  savedInstanceState savedInstanceState
     */
    abstract fun onActivityCreated(savedInstanceState: Bundle?)


    abstract fun setupListener()

    /**
     * 显示Loading
     */
    open fun showLoading(msg: String? = null) {
//        ToastUtils.showLong(msg)
    }

    /**
     * 隐藏Loading
     */
    open fun dismissLoading() {
//        ToastUtils.showLong("hideLoading")
    }

    /**
     * Create ViewBinding
     */
    @Suppress("UNCHECKED_CAST")
    open fun createViewBinding(): VB {
        val actualGenericsClass = findActualGenericsClass<VB>(ViewBinding::class.java)
            ?: throw NullPointerException("Can not find a ViewBinding Generics in ${javaClass.simpleName}")

        // 1. 遍历所有名为"inflate"的方法，筛选出符合条件的方法
        val inflateMethods = actualGenericsClass.methods
            .filter { it.name == "inflate" && Modifier.isStatic(it.modifiers) } // 必须是静态方法
            .filter { it.returnType == actualGenericsClass } // 返回值必须是当前Binding类型

        if (inflateMethods.isEmpty()) {
            throw RuntimeException("No static inflate method found in ${actualGenericsClass.simpleName}")
        }

        // 2. 尝试调用筛选后的方法（按参数数量从小到大匹配，避免复杂参数）
        val method = inflateMethods.sortedBy { it.parameterTypes.size }.firstOrNull()
            ?: throw RuntimeException("No valid inflate method found in ${actualGenericsClass.simpleName}")

        return try {
            // 根据方法参数数量，动态传入对应参数
            val args = when (method.parameterTypes.size) {
                1 -> arrayOf(layoutInflater) // 参数：LayoutInflater
                2 -> arrayOf(
                    layoutInflater,
                    null as ViewGroup?
                ) // 参数：LayoutInflater, ViewGroup（传null）
                3 -> arrayOf(
                    layoutInflater,
                    null as ViewGroup?,
                    false
                ) // 参数：LayoutInflater, ViewGroup, boolean
                else -> throw RuntimeException("Unsupported inflate method parameters count: ${method.parameterTypes.size}")
            }
            // 调用方法并返回Binding实例
            method.invoke(null, *args) as VB
        } catch (e: Exception) {
            e.printStackTrace()
            // 打印所有可用inflate方法的详细信息，方便后续调试
            val methodDetails = inflateMethods.joinToString("\n") { method ->
                val paramTypes = method.parameterTypes.joinToString { it.simpleName }
                "${method.name}($paramTypes) -> ${method.returnType.simpleName}"
            }
            throw RuntimeException(
                "ViewBinding inflate error in ${actualGenericsClass.simpleName}\n" +
                        "Available inflate methods:\n$methodDetails",
                e
            )
        }
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

    // 权限申请回调接口
    interface PermissionCallback {
        fun onPermissionGranted() // 所有权限都被授予
        fun onPermissionDenied(deniedPermissions: List<String>) // 有权限被拒绝
        fun onPermissionDeniedPermanently(deniedPermissions: List<String>) // 有权限被永久拒绝
    }


    /**
     * 申请权限
     * @param permissions 需要申请的权限数组
     * @param callback 权限申请结果回调
     */
    fun requestPermissions(permissions: Array<String>, callback: PermissionCallback) {
        this.permissionCallback = callback

        // 筛选出未授予的权限
        val needRequestPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (needRequestPermissions.isEmpty()) {
            // 所有权限已授予
            callback.onPermissionGranted()
            return
        }

        // 申请权限
        ActivityCompat.requestPermissions(
            this,
            needRequestPermissions.toTypedArray(),
            PERMISSION_REQUEST_CODE
        )
    }

    /**
     * 处理权限申请结果
     */
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
                // 检查是否永久拒绝（用户勾选了"不再询问"）
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                    permanentlyDeniedPermissions.add(permissions[i])
                }
            }
        }

        when {
            deniedPermissions.isEmpty() -> {
                permissionCallback?.onPermissionGranted()
            }

            permanentlyDeniedPermissions.isNotEmpty() -> {
                permissionCallback?.onPermissionDeniedPermanently(permanentlyDeniedPermissions)
            }

            else -> {
                permissionCallback?.onPermissionDenied(deniedPermissions)
            }
        }

        // 避免内存泄漏
        permissionCallback = null
    }


    // 查找合适的inflate方法
    private fun findInflateMethod(vbClass: Class<VB>): Method {
        // 新增：打印当前查找的Binding类名和所有方法，确认是否找对类
//        Log.e("BaseActivity", "当前查找的Binding类：${vbClass.name}")
//        Log.e("BaseActivity", "该类的所有方法：")
//        vbClass.methods.forEach { method ->
//            val paramTypes = method.parameterTypes.joinToString { it.simpleName }
//            Log.e(
//                "BaseActivity",
//                "方法名：${method.name}，参数：($paramTypes)，返回值：${method.returnType.simpleName}"
//            )
//        }
        // 原有逻辑：查找3种参数组合的inflate方法
        val paramTypesList = listOf(
            arrayOf(LayoutInflater::class.java),
            arrayOf(LayoutInflater::class.java, ViewGroup::class.java),
            arrayOf(
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.javaPrimitiveType
            )
        )

        for (paramTypes in paramTypesList) {
            try {
                return vbClass.getMethod("inflate", *paramTypes)
            } catch (e: NoSuchMethodException) {
                // 新增：打印未找到的方法，方便调试
                val paramStr = paramTypes.joinToString { it?.simpleName ?: "it is null" }
                Log.w("BaseActivity", "未找到inflate($paramStr)方法，继续尝试...")
                continue
            }
        }
        throw NoSuchMethodException("未找到合适的inflate方法，VB类: ${vbClass.simpleName}")
    }

    // 调用inflate方法创建Binding实例
    private fun invokeInflateMethod(method: Method): VB {
        return when (method.parameterTypes.size) {
            1 -> method.invoke(null, layoutInflater) as VB
            2 -> method.invoke(null, layoutInflater, null as ViewGroup?) as VB
            3 -> method.invoke(null, layoutInflater, null as ViewGroup?, false) as VB
            else -> throw IllegalArgumentException("不支持的inflate方法参数数量")
        }
    }

    // 获取泛型实际类型的工具方法
    private fun <T> getActualTypeClass(index: Int): Class<T> {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[index]
        return type as Class<T>
    }


}