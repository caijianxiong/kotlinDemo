package com.cjx.kotlin.base.net

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cjx.kotlin.base.BaseResponse

class ResponseMutableLiveData<T>:MutableLiveData<T>(){
    fun postValue(response: BaseResponse<T>) {


    }
}