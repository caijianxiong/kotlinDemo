package com.cjx.kotlin.base.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjx.kotlin.base.IBaseViewModel
import com.cjx.kotlin.base.SingleLiveEvent
import com.cjx.kotlin.base.model.BaseRepository
import com.cjx.kotlin.base.net.LoadingState
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.cancel
import java.lang.reflect.ParameterizedType

abstract class BaseViewModel<T : BaseRepository> : ViewModel(), IBaseViewModel {

    // 推荐使用viewModelScope，保留compositeDisposable用于兼容RxJava旧代码
    open val compositeDisposable = CompositeDisposable()

    // UI状态事件总线 (如果项目准备迁移Flow，建议废弃UIChangeLiveData改用SharedFlow)

    // 通过反射创建Repository
    // 优化建议：推荐使用构造函数注入Repository (如Hilt/Koin)，去除此类反射
    protected val repository: T by lazy(LazyThreadSafetyMode.NONE) {
        createRepository()
    }

    val loadingDataState: LiveData<LoadingState> by lazy {
        repository.loadingStateLiveData
    }


    override fun onCleared() {
        Log.d("TAG", "onCleared: ")
        compositeDisposable.clear()
        // viewModelScope会自动取消，无需手动调用
        super.onCleared()
    }

    /**
     * 创建Repository
     * 警告：这是反射创建，依赖于无参构造函数。建议逐步重构为依赖注入。
     */
    @Suppress("UNCHECKED_CAST")
    private fun createRepository(): T {
        // 使用反射获取泛型类型
        val repositoryClass = findActualGenericsClass<T>(BaseRepository::class.java)
            ?: throw IllegalStateException("Cannot determine BaseRepository generics type in ${javaClass.simpleName}")

        // 创建实例并验证类型
        return repositoryClass.getDeclaredConstructor().newInstance().also {
            require(it is BaseRepository) {
                "Created repository must be a BaseRepository instance"
            }
        } as T
    }

    private fun <T> Any.findActualGenericsClass(cls: Class<*>): Class<T>? {
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