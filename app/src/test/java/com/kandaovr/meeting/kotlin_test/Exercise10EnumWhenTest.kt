package com.kandaovr.meeting.kotlin_test

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 练习 10：枚举与 when。
 *
 * 目标：只实现 [canManageUsers]。
 *
 * 行为契约：只有 ADMIN 能管理用户；GUEST 和 MEMBER 都不能管理用户。
 * 约束：不要改动测试；用枚举分支清晰表达角色权限。
 */
class Exercise10EnumWhenTest {

    @Test
    fun adminCanManageUsers() {
        assertTrue(canManageUsers(Role.ADMIN))
    }

    @Test
    fun memberCannotManageUsers() {
        assertFalse(canManageUsers(Role.MEMBER))
    }

    @Test
    fun guestCannotManageUsers() {
        assertFalse(canManageUsers(Role.GUEST))
    }

    private fun canManageUsers(role: Role): Boolean =
        when (role) {
            Role.ADMIN -> true
            Role.MEMBER, Role.GUEST -> false
            else -> false
        }

    private enum class Role {
        GUEST,
        MEMBER,
        ADMIN
    }
}
