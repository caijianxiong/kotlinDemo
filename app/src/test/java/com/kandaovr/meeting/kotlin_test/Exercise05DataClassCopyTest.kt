package com.kandaovr.meeting.kotlin_test

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 练习 05：data class 的不可变更新。
 *
 * 目标：只实现 [upgrade]。
 *
 * 行为契约：返回 premium 为 true 的新 Profile；原 Profile 不能被修改；name 保持不变。
 * 约束：不要改动测试；使用 data class 已提供的复制能力。
 */
class Exercise05DataClassCopyTest {

    @Test
    fun upgradeReturnsPremiumProfile() {
        val profile = Profile("Ada", premium = false)

        assertTrue(upgrade(profile).premium)
    }

    @Test
    fun upgradeKeepsTheOriginalProfileUnchanged() {
        val profile = Profile("Ada", premium = false)

        upgrade(profile)

        assertFalse(profile.premium)
    }

    @Test
    fun upgradeKeepsTheExistingName() {
        assertEquals("Ada", upgrade(Profile("Ada", premium = false)).name)
    }

    private fun upgrade(profile: Profile): Profile =
        Profile(profile.name, true)

    private data class Profile(val name: String, val premium: Boolean)
}
