package com.kandaovr.meeting.kotlinDemo.network

import androidx.lifecycle.viewModelScope
import com.cjx.kotlin.base.model.LoginRepository
import com.cjx.kotlin.base.net.ResponseLiveData
import com.cjx.kotlin.base.net.ResponseMutableLiveData
import com.cjx.kotlin.base.vm.BaseViewModel
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel<LoginRepository>() {

    // 提供给Model层设置数据
    private val _loginLiveData: ResponseMutableLiveData<LoginResponse> = ResponseMutableLiveData()

    // 提供给View层观察数据
    val loginLiveData: ResponseLiveData<LoginResponse> = _loginLiveData

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


}