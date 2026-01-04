package com.cjx.feature.user.data.remote

import com.cjx.feature.user.data.model.User
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit 网络接口定义
 *
 * 这里的函数必须是 `suspend` 挂起函数，以支持 Kotlin Coroutines。
 */
interface UserApi {
    /**
     * 根据 ID 获取用户信息
     *
     * @param id 用户 ID
     * @return 返回 [User] 对象，Retrofit + Serialization 会自动解析 JSON
     */
    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: Long): User
}