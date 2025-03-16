package com.kandaovr.meeting.kotlinDemo.test.mvvm

import com.cjx.kotlin.base.BaseResponse
import com.cjx.kotlin.base.DataState
import com.cjx.kotlin.base.model.BaseRepository
import com.cjx.kotlin.base.net.ResponseMutableLiveData
import com.kandaovr.meeting.kotlinDemo.network.RetrofitManager

data class LoginResult(val dataState: DataState, val data: String)

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
                RetrofitManager.apiService.login(username,password)
            },
           responseLiveData, showLoading
        )
    }

    suspend fun getData(): BaseResponse<LoginResult> {
        val reponse = LoginResponse<LoginResult>().apply {
            errorCode = 0
            errorMsg = "success"
            data = LoginResult(DataState.STATE_SUCCESS, "success")
            dataState = DataState.STATE_SUCCESS
            exception = null
        }
        return reponse
    }
}