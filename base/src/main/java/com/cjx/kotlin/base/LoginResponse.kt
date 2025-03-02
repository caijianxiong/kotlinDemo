package com.cjx.kotlin.base

import com.cjx.kotlin.base.model.LoginResult

class LoginResponse (
    errorCode: Int = -1,
    errorMsg: String? = null,
    data: LoginResult? = null,
    dataState: DataState? = null,
    exception: Throwable? = null
) : BaseResponse<LoginResult>(errorCode, errorMsg, data, dataState, exception) {


}