package com.kandaovr.meeting.kotlin_test

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 练习 01：可空类型的安全处理。
 *
 * 目标：只实现 [normalizeNickname]，让所有测试通过。
 *
 * 行为契约：
 * - 输入为 null 时返回 "Guest"。
 * - 去除首尾空白后为空时返回 "Guest"。
 * - 其他输入返回去除首尾空白后的原始内容，保留大小写。
 *
 * 约束：不要改动测试；优先使用 Kotlin 的可空类型能力，而不是把 null 转成特殊字符串再判断。
 */
class Exercise01NullSafetyTest {

    @Test
    fun nullNicknameUsesDefault() {
        assertEquals("Guest", normalizeNickname(null))
    }

    @Test
    fun blankNicknameUsesDefault() {
        assertEquals("Guest", normalizeNickname(" \t\n "))
    }

    @Test
    fun validNicknameIsTrimmedAndCaseIsPreserved() {
        assertEquals("KoTlin", normalizeNickname("  KoTlin  "))
    }

    private fun normalizeNickname(raw: String?): String =
        raw?.trim()?.ifEmpty { "Guest" } ?: "Guest"
}


