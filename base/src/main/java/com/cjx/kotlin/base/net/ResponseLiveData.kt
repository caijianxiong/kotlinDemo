package com.cjx.kotlin.base.net

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/*
封装网络请求的类型
 */
abstract class ResponseLiveData<T>: LiveData<BaseResponse<T>>() {
    override fun observe(owner: LifecycleOwner, observer: Observer<in BaseResponse<T>>) {
        super.observe(owner, observer)
    }

}