package com.cjx.kotlin.base.net

import com.cjx.kotlin.base.BaseResponse

class ResponseMutableLiveData<T>:ResponseLiveData<T>(){
    public override fun postValue(value: BaseResponse<T>) {
        super.postValue(value)
    }
}