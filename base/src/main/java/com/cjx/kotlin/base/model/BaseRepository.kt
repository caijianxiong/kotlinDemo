package com.cjx.kotlin.base.model

import androidx.lifecycle.MutableLiveData
import com.cjx.kotlin.base.BaseResponse
import com.cjx.kotlin.base.DataState
import com.cjx.kotlin.base.net.LoadingState
import com.cjx.kotlin.base.net.ResponseMutableLiveData
import com.cjx.kotlin.base.log.ClzLogger

/**
 * MODEL 层
 */
open class BaseRepository// 显式声明无参构造函数
{

    // Loading 状态的 LiveData
    val loadingStateLiveData: MutableLiveData<LoadingState> by lazy {
        MutableLiveData<LoadingState>()
    }

    /**
     * 发起请求
     *  @param  block 真正执行的函数回调
     *  @param responseLiveData 观察请求结果的LiveData
     */
    suspend fun <T : Any> executeRequest(
        block: suspend () -> BaseResponse<T>,
        responseLiveData: ResponseMutableLiveData<T>,
        showLoading: Boolean = true,
        loadingMsg: String? = null,
    ) {
        var response: BaseResponse<T> = BaseResponse()
        try {
            if (showLoading) {
                loadingStateLiveData.postValue(LoadingState(loadingMsg, DataState.STATE_LOADING))
            }
            response = block.invoke()
            if (response.errorCode == BaseResponse.ERROR_CODE_SUCCESS) {
                if (isEmptyData(response.data)) {
                    response.dataState = DataState.STATE_EMPTY
                } else {
                    response.dataState = DataState.STATE_SUCCESS
                }
            } else {
                response.dataState = DataState.STATE_FAILED
// throw ServerResponseException(response.errorCode, response.errorMsg)
            }
        } catch (e: Exception) {
            response.dataState = DataState.STATE_ERROR
            response.exception = e
            ClzLogger.e(this, e, "executeRequest error")
        } finally {
            ClzLogger.d(this, "executeRequest final")
            responseLiveData.postValue(response)
            if (showLoading) {
                loadingStateLiveData.postValue(LoadingState(loadingMsg, DataState.STATE_FINISH))
            }
        }
    }

    private fun <T> isEmptyData(data: T?): Boolean {
        return data == null || data is List<*> && (data as List<*>).isEmpty()
    }


}