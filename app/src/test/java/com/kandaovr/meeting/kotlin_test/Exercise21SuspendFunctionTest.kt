package com.kandaovr.meeting.kotlin_test

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 练习 21：调用 suspend 函数。
 *
 * 目标：只实现 [loadUserNames]。
 * 行为契约：按输入 id 的原有顺序返回 "user-<id>"；空输入返回空列表；函数保持 suspend 签名，以便由协程调用者使用。
 * 约束：不要改动测试；理解测试中的 runBlocking 仅用于从普通 JUnit 方法调用 suspend 函数。
 */
class Exercise21SuspendFunctionTest {

    @Test
    fun loadsNamesInTheSameOrderAsIds() = runBlocking {
        assertEquals(listOf("user-7", "user-2", "user-9"), loadUserNames(listOf(7, 2, 9)))
    }

    @Test
    fun emptyInputProducesEmptyList() = runBlocking {
        assertEquals(emptyList<String>(), loadUserNames(emptyList()))
    }

    @Test
    fun supportsRepeatedIds() = runBlocking {
        assertEquals(listOf("user-3", "user-3"), loadUserNames(listOf(3, 3)))
    }

    private suspend fun loadUserNames(ids: List<Int>): List<String> =
        TODO("Implement the exercise")
}
