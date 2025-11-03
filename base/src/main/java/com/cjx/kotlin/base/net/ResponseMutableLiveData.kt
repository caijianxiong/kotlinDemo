package com.cjx.kotlin.base.net

class ResponseMutableLiveData<T>:ResponseLiveData<T>(){
    public override fun postValue(value: BaseResponse<T>) {
        super.postValue(value)
    }
}