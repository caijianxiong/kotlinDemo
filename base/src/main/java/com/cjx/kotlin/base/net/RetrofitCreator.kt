package com.cjx.kotlin.base.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitCreator {
    private val mOkClient =
        OkHttpClient.Builder().callTimeout(Config.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(Config.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(Config.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(Config.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true).followRedirects(false)
            .addInterceptor(HttpHeaderInterceptor())
//            .addInterceptor(LogInterceptor())
            .build()

    private fun getRetrofitBuilder(baseUrl: String): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(mOkClient)
            .addConverterFactory(GsonConverterFactory.create())
    }

    /**
     * Create Api service
     *  @param  cls Api Service
     *  @param  baseUrl Base Url
     */
    fun <T> getApiService(cls: Class<T>, baseUrl: String): T {
        val retrofit = getRetrofitBuilder(
            baseUrl
        ).build()
        return retrofit.create(cls)
    }

}