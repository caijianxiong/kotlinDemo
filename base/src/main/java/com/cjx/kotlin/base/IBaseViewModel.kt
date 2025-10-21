package com.cjx.kotlin.base

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

interface IBaseViewModel : DefaultLifecycleObserver {
    // 重写 DefaultLifecycleObserver 的默认方法（无需注解）
    override fun onCreate(owner: LifecycleOwner) {
        // 可选：添加默认实现，子类可覆盖
    }

    override fun onStart(owner: LifecycleOwner) {
        // 可选：添加默认实现
    }

    override fun onResume(owner: LifecycleOwner) {
        // 可选：添加默认实现
    }

    override fun onPause(owner: LifecycleOwner) {
        // 可选：添加默认实现
    }

    override fun onStop(owner: LifecycleOwner) {
        // 可选：添加默认实现
    }

    override fun onDestroy(owner: LifecycleOwner) {
        // 可选：添加默认实现
    }
}