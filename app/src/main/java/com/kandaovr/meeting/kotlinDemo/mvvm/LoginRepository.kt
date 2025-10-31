package com.kandaovr.meeting.kotlinDemo.mvvm

import android.util.Log
import com.cjx.kotlin.base.BaseResponse
import com.cjx.kotlin.base.DataState
import com.cjx.kotlin.base.model.BaseRepository
import com.cjx.kotlin.base.net.LoadingState
import com.cjx.kotlin.base.net.ResponseMutableLiveData
import com.kandaovr.meeting.kotlinDemo.network.RetrofitManager
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class LoginResult(
    val data: String,
    val username: String,
    var password: String
)

// 数据模型：请求结果
data class RequestResult(
    val requestId: Int,
    val data: String
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

    fun sendRxRequest(): Disposable {
        val requestCount = 5
        val requestObservables = List(requestCount) { index ->
            createNetworkRequestObservable(index + 1)
        }
        return Observable.concat(requestObservables).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Log.d("sendRxRequest", " success: ${result.requestId}")
                },
                { error ->
                    Log.d("sendRxRequest", " error: ${error.message}")

                },
                {
                    Log.d("sendRxRequest", " onComplete:")

                })

    }

    // 模拟单个网络请求
    private fun createNetworkRequestObservable(requestId: Int): Observable<RequestResult> {
        return Observable.create { emitter ->
            try {
                // 模拟网络延迟（1秒）
                Thread.sleep(2000)

                // 检查是否已取消，避免内存泄漏
                if (!emitter.isDisposed) {
                    emitter.onNext(
                        RequestResult(
                            requestId = requestId,
                            data = "响应数据 $requestId"
                        )
                    )
                    emitter.onComplete()
                }
            } catch (e: InterruptedException) {
                // 取消时会触发中断，此时不发送错误
                if (!emitter.isDisposed) {
                    emitter.onError(e)
                }
            }
        }
    }

}