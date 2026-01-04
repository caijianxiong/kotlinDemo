package com.cjx.feature.user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjx.feature.user.data.model.User
import com.cjx.feature.user.domain.repository.UserRepository
import com.cjx.feature.user.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 用户详情页面的 ViewModel
 *
 * 负责管理 UI 状态 ([UserUiState]) 并处理业务逻辑交互。
 * 使用 @HiltViewModel 注解，以便 Hilt 可以将依赖项注入到 ViewModel 中。
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val repository: UserRepository
) : ViewModel() {

    // 内部可变的 StateFlow，用于更新状态
    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    
    // 公开不可变的 StateFlow，供 UI 观察
    // StateFlow 是一种特殊的 Flow，它总是持有一个当前值，并且是热流（Hot Flow）。
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    /**
     * 加载用户数据
     *
     * 启动协程执行异步操作。
     */
    fun loadUser(userId: Long) {
        viewModelScope.launch {
            _uiState.value = UserUiState.Loading
            try {
                // 1. 触发从网络刷新数据
                // 这是一个 suspend 函数，会挂起直到网络请求完成或失败
                repository.refreshUser(userId)
                
                // 2. 观察本地数据库的数据流
                // 当 refreshUser 更新数据库后，这里的 Flow 会自动发射最新的 User 对象
                getUserUseCase(userId)
                    .catch { e -> _uiState.value = UserUiState.Error(e.message ?: "Unknown error") }
                    .collect { user ->
                        if (user != null) {
                            _uiState.value = UserUiState.Success(user)
                        } else {
                            _uiState.value = UserUiState.Error("User not found")
                        }
                    }
            } catch (e: Exception) {
                // 处理网络请求异常（例如无网络连接）
                _uiState.value = UserUiState.Error(e.message ?: "Network error")
            }
        }
    }
}

/**
 * UI 状态密封接口
 *
 * 定义了界面可能处于的所有状态。UI 层只需要根据当前状态渲染对应的内容。
 * 这种模式称为 "State Pattern" 或 "MVI (Model-View-Intent)" 的一部分。
 */
sealed interface UserUiState {
    /** 加载中状态 */
    object Loading : UserUiState
    /** 成功状态，携带用户数据 */
    data class Success(val user: User) : UserUiState
    /** 错误状态，携带错误信息 */
    data class Error(val message: String) : UserUiState
}