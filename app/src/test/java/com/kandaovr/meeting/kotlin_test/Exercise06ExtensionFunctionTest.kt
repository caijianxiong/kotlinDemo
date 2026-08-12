package com.kandaovr.meeting.kotlin_test

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 练习 06：扩展函数。
 *
 * 目标：只实现 [String.isValidInvitationCode]。
 *
 * 行为契约：邀请码恰好为 6 个字符，且每个字符都是数字时返回 true；其他情况返回 false。
 * 约束：不要改动测试；让校验能力以 String 的扩展函数形式出现。
 */
class Exercise06ExtensionFunctionTest {

    @Test
    fun sixDigitsAreValid() {
        assertTrue("123456".isValidInvitationCode())
    }

    @Test
    fun wrongLengthIsInvalid() {
        assertFalse("12345".isValidInvitationCode())
    }

    @Test
    fun nonDigitsAreInvalid() {
        assertFalse("12A456".isValidInvitationCode())
    }

    private fun String.isValidInvitationCode(): Boolean = length == 6 && all { it.isDigit() }

//    {
//        if (length != 6) {
//            return false
//        }
//        forEach {
//            if (it.isDigit().not()) {
//                return false
//            }
//        }
//        return true
//    }
}
