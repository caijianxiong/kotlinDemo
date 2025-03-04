package com.kandaovr.meeting.kotlinDemo.network

import com.cjx.kotlin.base.net.RetrofitCreator

object RetrofitManager {

    private const val BASE_URL = "https://www.wanandroid.com/"

    val apiService: ApiService by lazy {
        RetrofitCreator.getApiService(ApiService::class.java, BASE_URL)
    }


}

