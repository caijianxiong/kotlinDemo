package com.cjx.kotlin.base.net

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.cjx.kotlin.base.BaseResponse

abstract class ResponseLiveData<T>: LiveData<BaseResponse<T>>() {
    override fun observe(owner: LifecycleOwner, observer: Observer<in BaseResponse<T>>) {
        super.observe(owner, observer)
    }

}