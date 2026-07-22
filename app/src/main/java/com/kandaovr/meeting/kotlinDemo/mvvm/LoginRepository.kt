package com.kandaovr.meeting.kotlinDemo.mvvm

import android.util.Log
import com.cjx.kotlin.base.model.BaseRepository
import com.cjx.kotlin.base.net.BaseResponse
import com.cjx.kotlin.base.net.DataState
import com.cjx.kotlin.base.net.LoadingState
import com.cjx.kotlin.base.net.ResponseMutableLiveData
import com.kandaovr.meeting.kotlinDemo.network.RetrofitManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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

// 密封类定义 UI 状态（替代 LiveData 承载的状态数据）
sealed class LoginUiState {
    object Idle : LoginUiState() // 初始状态
    object Loading : LoginUiState() // 加载中
    data class Success(val result: LoginResult) : LoginUiState() // 成功
    data class Error(val message: String) : LoginUiState() // 失败
}

class LoginRepository : BaseRepository() {

    /**
     * Flow 教程 1：普通 Flow 是“冷流”。
     * 每次 collect 都会重新执行 flow { } 里面的代码，适合一次网络请求、数据库查询、文件读取等数据源。
     */
    fun loginByFlow(username: String, password: String): Flow<LoginResult> = flow {
        delay(1000)
        if (password == "error") {
            error("密码不能为 error")
        }
        emit(LoginResult("Flow 请求完成", username, password))
    }.flowOn(Dispatchers.IO)

    /**
     * Flow 教程 2：连续发射多个值。
     * 这里模拟一个接口请求的几个步骤，Activity 收集后可以直接追加到日志区域。
     */
    fun requestStepFlow(): Flow<String> = flow {
        emit("1. 创建请求参数")
        delay(600)
        emit("2. 切到 IO 线程执行耗时任务")
        delay(600)
        emit("3. 拿到结果并回到 UI 层展示")
    }.flowOn(Dispatchers.IO)

    /**
     * Flow 教程 3：倒计时 Flow。
     * Repository 只负责发射数据，是否保存最后一个值由 ViewModel 的 StateFlow 决定。
     */
    fun countDownFlow(from: Int): Flow<Int> = flow {
        for (second in from downTo 0) {
            emit(second)
            delay(1000)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun login(
        username: String,
        password: String,
        responseLiveData: ResponseMutableLiveData<LoginResult>,
        showLoading: Boolean = true
    ) {
        executeRequest(
            requestTask = {
                // 网络请求返回
                RetrofitManager.apiService.login(username, password)
            },
            responseLiveData,
            showLoading
        )
    }

    suspend fun loginTest(
        username: String,
        password: String,
        responseLiveData: ResponseMutableLiveData<LoginResult>,
        showLoading: Boolean = true
    ) {
        withContext(Dispatchers.IO) {
            val response = BaseResponse<LoginResult>()
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
