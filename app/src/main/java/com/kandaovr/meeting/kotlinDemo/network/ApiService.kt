package com.kandaovr.meeting.kotlinDemo.network

import com.kandaovr.meeting.kotlinDemo.test.mvvm.LoginResult
import com.kandaovr.meeting.kotlinDemo.test.mvvm.LoginResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("weather")
    suspend fun login(@Query("login") city: String, @Query("password") pwd: String): LoginResponse<LoginResult>

}