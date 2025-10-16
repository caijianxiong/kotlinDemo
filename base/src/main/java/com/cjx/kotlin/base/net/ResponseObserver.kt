package com.cjx.kotlin.base.net

import androidx.lifecycle.Observer
import com.cjx.kotlin.base.BaseResponse
import com.cjx.kotlin.base.DataState

abstract class ResponseObserver<T> : Observer<BaseResponse<T>> {
    final override fun onChanged(response: BaseResponse<T>) {
        response.let {
            when (response.dataState) {
                DataState.STATE_SUCCESS, DataState.STATE_EMPTY -> {
                    onSuccess(response.data)
                }

                DataState.STATE_ERROR -> {
                    onException(response.exception)
                }

                DataState.STATE_FAILED -> {
                    onFailure(response.errorMsg, response.errorCode)
                }

                else -> {}
            }
        }

    }

    private fun onException(exception: Throwable?) {
//        ToastUtils.showToast(exception.toString())
    }

    /**
     * 请求成功
     *  @param  data 请求数据
     */
    abstract fun onSuccess(data: T?)

    /**
     * 请求失败
     *  @param  errorCode 错误码
     *  @param  errorMsg 错误信息
     */
    open fun onFailure(errorMsg: String?, errorCode: Int) {
//        ToastUtils.showToast("Login Failed,errorCode:$errorCode,errorMsg:$errorMsg")
    }

}