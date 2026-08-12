package com.kandaovr.meeting.kotlin_test

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 练习 14：使用 associateBy 建立索引。
 *
 * 目标：只实现 [indexById]。
 * 行为契约：以用户 id 为键建立 Map；输入为空时返回空 Map；遇到重复 id 时保留列表中靠后的用户。
 * 约束：不要改动测试；使用 associateBy 表达“按属性建立索引”。
 */
class Exercise14AssociateByTest {

    @Test
    fun createsAnEntryForEveryDistinctId() {
        val users = listOf(User("u1", "Ada"), User("u2", "Linus"))

        assertEquals(mapOf("u1" to User("u1", "Ada"), "u2" to User("u2", "Linus")), indexById(users))
    }

    @Test
    fun laterUserReplacesEarlierDuplicateId() {
        val users = listOf(User("u1", "Ada"), User("u1", "Ada Lovelace"))

        assertEquals(User("u1", "Ada Lovelace"), indexById(users)["u1"])
    }

    @Test
    fun emptyInputProducesEmptyMap() {
        assertEquals(emptyMap<String, User>(), indexById(emptyList()))
    }

    private fun indexById(users: List<User>): Map<String, User> =
        users.associateBy { user -> user.id }

    private data class User(val id: String, val name: String)
}
