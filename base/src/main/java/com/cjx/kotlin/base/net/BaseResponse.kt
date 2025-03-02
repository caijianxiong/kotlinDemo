package com.cjx.kotlin.base

open class BaseResponse<T>(
    var errorCode: Int = -1,
    var errorMsg: String? = null,
    var data: T? = null,
    var dataState: DataState? = null,
    var exception: Throwable? = null
) {

    companion object {
        const val ERROR_CODE_SUCCESS = 0
    }

    val success: Boolean
        get() = errorCode == ERROR_CODE_SUCCESS

}

/**
 * 网络请求状态
 */
enum class DataState {
    STATE_LOADING, // 开始请求
    STATE_SUCCESS, // 服务器请求成功
    STATE_EMPTY, // 服务器返回数据为null
    STATE_FAILED, // 接口请求成功但是服务器返回error
    STATE_ERROR, // 请求失败
    STATE_FINISH, // 请求结束
}