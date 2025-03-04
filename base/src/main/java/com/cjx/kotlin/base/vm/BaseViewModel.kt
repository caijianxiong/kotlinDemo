package com.cjx.kotlin.base.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cjx.kotlin.base.SingleLiveEvent
import com.cjx.kotlin.base.model.BaseRepository
import com.cjx.kotlin.base.net.LoadingState
import com.trello.rxlifecycle2.LifecycleProvider
import java.lang.ref.WeakReference
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class BaseViewModel<T : BaseRepository> : ViewModel() {

    private var mLifecycle: WeakReference<LifecycleProvider<*>>? = null
    private var uc: UIChangeLiveData? = null

    protected val repository: T by lazy(LazyThreadSafetyMode.NONE) {
        createRepository()
    }

    val loadingDataState: LiveData<LoadingState> by lazy {
        repository.loadingStateLiveData
    }

    /**
     * 创建Repository
     */
    @Suppress("UNCHECKED_CAST")
    open fun createRepository(): T {
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


    companion object {

        class UIChangeLiveData : SingleLiveEvent<Any?>() {

            private var startActivityEvent: SingleLiveEvent<Map<String, Any>>? = null
            private var finishEvent: SingleLiveEvent<Void>? = null
            private var onBackPressedEvent: SingleLiveEvent<Void>? = null
            private var setResultEvent: SingleLiveEvent<Map<String, String>>? = null
            private var finishResult: SingleLiveEvent<Int>? = null
            private var startActivityForFragment: SingleLiveEvent<Map<String, Any>>? = null
            private var setResultFragment: SingleLiveEvent<Map<String, Any>>? = null


            fun getResultFragment(): SingleLiveEvent<Map<String, Any>> {
                return createLiveData(setResultFragment).also {
                    setResultFragment = it
                }
            }

            fun getStartActivityForFragment(): SingleLiveEvent<Map<String, Any>> {
                return createLiveData(startActivityForFragment).also {
                    startActivityForFragment = it
                }
            }

            fun getFinishResult(): SingleLiveEvent<Int> {
                return createLiveData(finishResult).also {
                    finishResult = it
                }
            }

            fun getStartActivityEvent(): SingleLiveEvent<Map<String, Any>> {
                return createLiveData(startActivityEvent).also {
                    startActivityEvent = it
                }


            }

            fun getSetResultEvent(): SingleLiveEvent<Map<String, String>> {
                return createLiveData(setResultEvent).also {
                    setResultEvent = it
                }
            }

            fun getFinishEvent(): SingleLiveEvent<Void> {
                return createLiveData(finishEvent).also {
                    finishEvent = it
                }
            }

            fun getOnBackPressedEvent(): SingleLiveEvent<Void> {
                return createLiveData(onBackPressedEvent).also {
                    onBackPressedEvent = it
                }
            }


            private fun <T> createLiveData(liveData: SingleLiveEvent<T>?): SingleLiveEvent<T> {

                var mLive: SingleLiveEvent<T>? = liveData
                liveData?.let {
                    return mLive!!
                } ?: let {
                    mLive = SingleLiveEvent()
                }


                return mLive!!
            }


        }

        object ParameterField {
            const val CLASS = "CLASS"
            const val CANONICAL_NAME = "CANONICAL_NAME"
            const val BUNDLE = "BUNDLE"
            const val REQUEST = "REQUEST"
            const val REQEUST_DEFAULT = 1
        }
    }

}