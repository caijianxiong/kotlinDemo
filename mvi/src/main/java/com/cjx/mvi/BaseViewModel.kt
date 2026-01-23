package com.cjx.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * MVI 架构的通用 ViewModel 基类。
 * 它封装了 State、Action 和 Event 的处理逻辑。
 *
 * @param S UI 状态 (State)，必须实现 IState
 * @param A 用户意图 (Action)，必须实现 IAction
 * @param E 一次性事件 (Event)，必须实现 IEvent
 */
abstract class BaseViewModel<S : IState, A : IAction, E : IEvent> : ViewModel() {

    // 初始状态，必须由子类实现
    protected abstract val initialState: S

    // --- State --- 
    private val _uiState: MutableStateFlow<S> by lazy { MutableStateFlow(initialState) }
    val uiState = _uiState.asStateFlow()

    // --- Event --- 
    private val _event = Channel<E>()
    val event = _event.receiveAsFlow()

    /**
     * 供 UI 调用的唯一入口，用于分发用户意图 (Action)。
     */
    fun dispatch(action: A) {
        viewModelScope.launch {
            handleAction(action)
        }
    }

    /**
     * 子类必须实现此方法来处理具体的业务逻辑。
     */
    protected abstract suspend fun handleAction(action: A)

    /**
     * 用于更新 UI 状态。
     */
    protected fun setState(newState: S) {
        _uiState.value = newState
    }

    /**
     * 用于发送一次性事件。
     */
    protected fun sendEvent(event: E) {
        viewModelScope.launch { _event.send(event) }
    }
}