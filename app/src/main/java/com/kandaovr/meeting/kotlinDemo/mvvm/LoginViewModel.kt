package com.kandaovr.meeting.kotlinDemo.mvvm

import androidx.lifecycle.viewModelScope
import com.cjx.kotlin.base.net.ResponseLiveData
import com.cjx.kotlin.base.net.ResponseMutableLiveData
import com.cjx.kotlin.base.vm.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

sealed class LoginUiEvent {
    data class Toast(val message: String) : LoginUiEvent()
    object NavigateHome : LoginUiEvent()
}

class LoginViewModel : BaseViewModel<LoginRepository>() {

    // 提供给 Model 层设置数据（可改变 LiveData 数据）
    private val _loginLiveData: ResponseMutableLiveData<LoginResult> = ResponseMutableLiveData()

    // 提供给 View 层观察数据（不可改变 LiveData 数据）
    val loginLiveData: ResponseLiveData<LoginResult> = _loginLiveData

    // StateFlow：保存“当前 UI 状态”，新订阅者会立即收到最新值。
    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    // StateFlow：保存倒计时最后一个值，适合页面文本、进度等可恢复状态。
    private val _countDownSecond = MutableStateFlow(0)
    val countDownSecond: StateFlow<Int> = _countDownSecond.asStateFlow()

    // StateFlow：教程日志，屏幕旋转后依然能拿到最后一次列表。
    private val _flowLogs = MutableStateFlow<List<String>>(emptyList())
    val flowLogs: StateFlow<List<String>> = _flowLogs.asStateFlow()

    // SharedFlow：发送一次性 UI 事件，比如 Toast、导航、弹窗；不保存“当前状态”。
    private val _loginEvent = MutableSharedFlow<LoginUiEvent>(extraBufferCapacity = 1)
    val loginEvent: SharedFlow<LoginUiEvent> = _loginEvent.asSharedFlow()

    /**
     * StateFlow + Flow：用 Flow 执行登录请求，用 StateFlow 保存页面状态。
     */
    fun loginByFlow(username: String, password: String) {
        viewModelScope.launch {
            repository.loginByFlow(username, password)
                .onStart {
                    _loginState.value = LoginUiState.Loading
                    appendFlowLog("StateFlow", "登录状态 = Loading")
                }
                .catch { throwable ->
                    val message = throwable.message ?: "登录失败"
                    _loginState.value = LoginUiState.Error(message)
                    appendFlowLog("Flow.catch", message)
                    _loginEvent.emit(LoginUiEvent.Toast(message))
                }
                .collect { result ->
                    _loginState.value = LoginUiState.Success(result)
                    appendFlowLog("StateFlow", "登录状态 = Success，${result.username}")
                    _loginEvent.emit(LoginUiEvent.Toast("SharedFlow：登录成功 Toast 只消费一次"))
                }
        }
    }

    /**
     * 普通 Flow：每点一次按钮都会重新开始 collect，因此 requestStepFlow 会重新执行。
     */
    fun startColdFlowDemo() {
        viewModelScope.launch {
            repository.requestStepFlow()
                .onStart { appendFlowLog("Flow", "开始 collect，冷流开始执行") }
                .catch { throwable -> appendFlowLog("Flow.catch", throwable.message ?: "未知异常") }
                .collect { step -> appendFlowLog("Flow", step) }
        }
    }

    /**
     * Flow + StateFlow：Repository 发射倒计时，ViewModel 把最后一个值保存成页面状态。
     */
    fun startCountDown() {
        viewModelScope.launch {
            repository.countDownFlow(5)
                .onStart { appendFlowLog("Flow", "倒计时开始") }
                .collect { second ->
                    _countDownSecond.value = second
                    appendFlowLog("StateFlow", "倒计时 = $second")
                }
        }
    }

    /**
     * SharedFlow：主动发送一次性事件。它适合 Toast/导航，不适合保存页面文本状态。
     */
    fun sendSharedFlowEvent() {
        _loginEvent.tryEmit(LoginUiEvent.Toast("SharedFlow：这是一次性事件，不会像 StateFlow 一样保存最新状态"))
        appendFlowLog("SharedFlow", "发送 Toast 事件")
    }

    fun clearFlowLog() {
        _flowLogs.value = emptyList()
        _loginState.value = LoginUiState.Idle
        _countDownSecond.value = 0
    }

    /**
     * Login
     *  @param  username 用户名
     *  @param  password 密码
     */
    fun login(username: String, password: String) {
        viewModelScope.launch {
            repository.login(username, password, _loginLiveData)
        }
    }

    fun loginTest(username: String, password: String) {
        viewModelScope.launch {
            repository.loginTest(username, password, _loginLiveData)
        }
    }

    fun sendNetWorkRequest() {
        // rx 请求添加
        compositeDisposable.add(repository.sendRxRequest())
        // 页面销毁时 ViewModel 回调取消未执行请求
    }

    private fun appendFlowLog(tag: String, message: String) {
        _flowLogs.value = (_flowLogs.value + "[$tag] $message").takeLast(8)
    }

}
