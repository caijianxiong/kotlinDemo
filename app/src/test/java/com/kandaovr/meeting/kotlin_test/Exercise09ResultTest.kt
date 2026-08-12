package com.kandaovr.meeting.kotlin_test

import androidx.core.text.isDigitsOnly
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 练习 09：Result 与失败建模。
 *
 * 目标：只实现 [parsePort]。
 *
 * 行为契约：去除首尾空白后，将 1 到 65535 的整数解析为成功结果；非数字、0 和超过范围的值返回失败结果。
 * 约束：不要改动测试；用 Result 表示成功或失败，而不是用特殊数字表示错误。
 */
class Exercise09ResultTest {

    @Test
    fun validPortProducesSuccess() {
        assertEquals(8080, parsePort(" 8080 ").getOrThrow())
    }

    @Test
    fun zeroProducesFailure() {
        assertTrue(parsePort("0").isFailure)
    }

    @Test
    fun tooLargePortProducesFailure() {
        assertTrue(parsePort("65536").isFailure)
    }

    @Test
    fun nonNumericPortProducesFailure() {
        assertTrue(parsePort("http").isFailure)
    }

    private fun parsePort(raw: String): Result<Int> =
        runCatching {
            require(raw.isNotEmpty())
            val trimed = raw.trimStart().trimEnd()
            require(trimed.all { it.isDigit() })
            val port = trimed.toInt()
            require(port in 1..65535)
            port
        }

}
