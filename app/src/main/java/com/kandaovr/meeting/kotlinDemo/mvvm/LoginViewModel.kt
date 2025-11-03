package com.kandaovr.meeting.kotlinDemo.mvvm

import androidx.lifecycle.viewModelScope
import com.cjx.kotlin.base.net.ResponseLiveData
import com.cjx.kotlin.base.net.ResponseMutableLiveData
import com.cjx.kotlin.base.vm.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel<LoginRepository>() {

    // 提供给Model层设置数据(可改变livedata数据)
    private val _loginLiveData: ResponseMutableLiveData<LoginResult> = ResponseMutableLiveData()

    // 提供给View层观察数据（不可改变数据）
    val loginLiveData: ResponseLiveData<LoginResult> = _loginLiveData

    // 1. 用 StateFlow 存储登录状态（替代普通 LiveData）
    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow() // 暴露只读接口


    // 发射流状态
    fun loginByFlow(username: String, password: String) {
        viewModelScope.launch {
            // 发送“加载中”状态
            _loginState.value = LoginUiState.Loading
            try {
                // 模拟网络请求
//                val result = repository.loginTest(username, password)
//                if (result.isSuccess) {
//                    // 登录成功：更新状态 + 发送导航事件
//                    _loginState.value = LoginUiState.Success(result.data)
//                } else {
//                    // 登录失败：更新状态
//                    _loginState.value = LoginUiState.Error(result.errorMsg)
//                }
            } catch (e: Exception) {
                _loginState.value = LoginUiState.Error(e.message ?: "登录失败")
            }
        }
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
        // rx请求添加
        compositeDisposable.add(repository.sendRxRequest())
        // 页面销毁时viewMode回调取消未执行请求
    }


}