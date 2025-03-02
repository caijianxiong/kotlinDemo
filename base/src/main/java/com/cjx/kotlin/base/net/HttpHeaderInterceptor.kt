package com.cjx.kotlin.base.net

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HttpHeaderInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        // 获取原始请求
        val originalRequest: Request = chain.request()
        // 对原始请求做一些处理
        return chain.proceed(originalRequest)
    }
}