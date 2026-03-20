package com.kandaovr.mvplibrary.base

import java.lang.ref.WeakReference

abstract class BasePresenter<V : IBaseView, M : IBaseModel>(protected val model:M) : IBasePresenter<V> {
    // 持有View的弱引用，避免内存泄漏

    private var viewRef: WeakReference<V>? = null
    /**
     * 获取View实例（需判空）
     */
    protected val view: V?
        get() = viewRef?.get()

    override fun attachView(view: V) {
        // presenter 持有model和view
        viewRef = WeakReference(view)
    }

    override fun detachView() {
        viewRef?.clear()
        viewRef = null
    }

    /**
     * 检查View是否存活
     */
    protected fun isViewAttached(): Boolean {
        return viewRef?.get() != null
    }
}