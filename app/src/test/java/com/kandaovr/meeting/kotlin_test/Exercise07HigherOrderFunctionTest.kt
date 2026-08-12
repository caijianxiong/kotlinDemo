package com.kandaovr.meeting.kotlin_test

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * 练习 07：泛型高阶函数。
 *
 * 目标：只实现 [firstMatch]。
 *
 * 行为契约：返回列表中第一个让 predicate 返回 true 的元素；没有匹配项或列表为空时返回 null。
 * 约束：不要改动测试；函数必须保持泛型，不能只处理某一种具体类型。
 */
class Exercise07HigherOrderFunctionTest {

    @Test
    fun returnsTheFirstMatchingNumber() {
        assertEquals(4, firstMatch(listOf(1, 3, 4, 6)) { it % 2 == 0 })
    }

    @Test
    fun returnsNullWhenNothingMatches() {
        assertNull(firstMatch(listOf(1, 3, 5)) { it % 2 == 0 })
    }

    @Test
    fun worksForStringsAsWell() {
        assertEquals("kotlin", firstMatch(listOf("java", "kotlin", "swift")) { it.startsWith("kot") })
    }

    private fun <T> firstMatch(items: List<T>, predicate: (T) -> Boolean): T? =
        items.firstOrNull { predicate(it) }
}
