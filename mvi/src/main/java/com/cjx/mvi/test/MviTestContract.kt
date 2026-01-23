package com.cjx.mvi.test

import com.cjx.mvi.IAction
import com.cjx.mvi.IEvent
import com.cjx.mvi.IState

/**
 * MVI 测试页面的契约类
 * 集中定义 State, Action, Event
 */

// 1. State: 描述 UI 当前的状态
data class MviTestState(
    val counter: Int = 0,
    val isLoading: Boolean = false
) : IState

// 2. Action: 描述用户的意图
sealed class MviTestAction : IAction {
    object IncrementCounter : MviTestAction()
    object DecrementCounter : MviTestAction()
    object FetchData : MviTestAction() // 模拟一个耗时操作
}

// 3. Event: 描述一次性事件
sealed class MviTestEvent : IEvent {
    data class ShowToast(val message: String) : MviTestEvent()
}
