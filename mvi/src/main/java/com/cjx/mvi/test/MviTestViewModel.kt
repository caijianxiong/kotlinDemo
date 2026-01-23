package com.cjx.mvi.test

import androidx.lifecycle.viewModelScope
import com.cjx.mvi.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MviTestViewModel : BaseViewModel<MviTestState, MviTestAction, MviTestEvent>() {

    override val initialState: MviTestState
        get() = MviTestState() // 提供初始状态

    override suspend fun handleAction(action: MviTestAction) {
        when (action) {
            is MviTestAction.IncrementCounter -> {
                // 更新状态：计数器加一
                setState(uiState.value.copy(counter = uiState.value.counter + 1))
            }
            is MviTestAction.DecrementCounter -> {
                // 更新状态：计数器减一
                setState(uiState.value.copy(counter = uiState.value.counter - 1))
            }
            is MviTestAction.FetchData -> {
                fetchData()
            }
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            // 开始加载，更新状态
            setState(uiState.value.copy(isLoading = true))
            sendEvent(MviTestEvent.ShowToast("Starting to fetch data..."))

            // 模拟网络请求
            delay(2000)

            // 加载完成，更新状态
            setState(uiState.value.copy(isLoading = false))
            sendEvent(MviTestEvent.ShowToast("Data fetched successfully!"))
        }
    }
}
