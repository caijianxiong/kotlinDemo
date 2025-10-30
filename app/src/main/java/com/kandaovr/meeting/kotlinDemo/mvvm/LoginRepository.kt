package com.kandaovr.meeting.kotlinDemo.mvvm

import com.cjx.kotlin.base.BaseResponse
import com.cjx.kotlin.base.DataState
import com.cjx.kotlin.base.model.BaseRepository
import com.cjx.kotlin.base.net.LoadingState
import com.cjx.kotlin.base.net.ResponseMutableLiveData
import com.kandaovr.meeting.kotlinDemo.network.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class LoginResult(
    val data: String,
    val username: String,
    var password: String
)

class LoginRepository : BaseRepository() {

    suspend fun login(
        username: String,
        password: String,
        responseLiveData: ResponseMutableLiveData<LoginResult>,
        showLoading: Boolean = true
    ) {
        executeRequest(
            // 网络请求返回
            {
                RetrofitManager.apiService.login(username, password)
            },
            responseLiveData, showLoading
        )
    }

    suspend fun loginTest(
        username: String,
        password: String,
        responseLiveData: ResponseMutableLiveData<LoginResult>,
        showLoading: Boolean = true
    ) {
        withContext(Dispatchers.IO) {
            var response = BaseResponse<LoginResult>()
            response.dataState = DataState.STATE_LOADING
            response.errorCode = 0
            response.data = LoginResult("开始请求", username, password)
            if (showLoading) loadingStateLiveData.postValue(
                LoadingState(
                    "请求中",
                    DataState.STATE_LOADING
                )
            )
            responseLiveData.postValue(response)
            Thread.sleep(3000)
            response.dataState = DataState.STATE_SUCCESS
            response.data = LoginResult("请求完成", username, password)
            if (showLoading) loadingStateLiveData.postValue(
                LoadingState(
                    "请求完成",
                    DataState.STATE_FINISH
                )
            )
            responseLiveData.postValue(response)

        }
    }


}