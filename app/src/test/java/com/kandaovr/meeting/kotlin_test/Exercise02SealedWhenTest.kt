package com.kandaovr.meeting.kotlin_test

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 练习 02：密封类与穷尽 when。
 *
 * 目标：只实现 [toStatusText]，让所有测试通过。
 *
 * 行为契约：
 * - [SyncState.Idle] 返回 "Idle"。
 * - [SyncState.Syncing] 返回 "Syncing"。
 * - [SyncState.Success] 返回 "Synced: <count>"。
 * - [SyncState.Failed] 返回 "Failed: <reason>"。
 *
 * 约束：不要改动状态类型或测试；利用密封类的有限状态集合，让 when 覆盖所有分支。
 */
class Exercise02SealedWhenTest {

    @Test
    fun idleStateHasIdleText() {
        assertEquals("Idle", toStatusText(SyncState.Idle))
    }

    @Test
    fun syncingStateHasSyncingText() {
        assertEquals("Syncing", toStatusText(SyncState.Syncing))
    }

    @Test
    fun successStateIncludesCount() {
        assertEquals("Synced: 3", toStatusText(SyncState.Success(3)))
    }

    @Test
    fun failedStateIncludesReason() {
        assertEquals("Failed: offline", toStatusText(SyncState.Failed("offline")))
    }

    private fun toStatusText(state: SyncState): String =
        when(state){
            SyncState.Idle -> "Idle"
            SyncState.Syncing -> "Syncing"
            is SyncState.Success -> "Synced: ${state.count}"
            is SyncState.Failed -> "Failed: ${state.reason}"
        }

    private sealed class SyncState {
        object Idle : SyncState()
        object Syncing : SyncState()
        data class Success(val count: Int) : SyncState()
        data class Failed(val reason: String) : SyncState()
    }
}
