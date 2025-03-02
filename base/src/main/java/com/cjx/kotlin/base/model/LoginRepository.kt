package com.cjx.kotlin.base.model

import com.cjx.kotlin.base.BaseResponse
import com.cjx.kotlin.base.DataState
import com.cjx.kotlin.base.LoginResponse
import com.cjx.kotlin.base.net.ResponseMutableLiveData

data class LoginResult(val dataState: DataState, val data: String)

class LoginRepository : BaseRepository() {

    suspend fun login(
        username: String,
        password: String,
        responseLiveData: ResponseMutableLiveData<LoginResponse>,
        showLoading: Boolean = true
    ) {
//        executeRequest(
//            // 网络请求返回
//            block = ::getData(), responseLiveData, showLoading
//        )
    }

    suspend fun getData(): BaseResponse<LoginResult> {
        val reponse = LoginResponse(
            0,
            "success",
            LoginResult(DataState.STATE_SUCCESS, "success"),
            DataState.STATE_SUCCESS, null
        )
        return reponse
    }
}