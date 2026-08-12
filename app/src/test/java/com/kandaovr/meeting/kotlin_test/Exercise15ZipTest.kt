package com.kandaovr.meeting.kotlin_test

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 练习 15：使用 zip 配对两个集合。
 *
 * 目标：只实现 [scoreLines]。
 * 行为契约：将同位置的名字和分数格式化为 "<name>: <score>"；只处理两个列表都存在的位置；空输入返回空列表。
 * 约束：不要改动测试；使用 zip 明确集合之间的按位置配对关系。
 */
class Exercise15ZipTest {

    @Test
    fun pairsNamesWithScoresInTheirOriginalOrder() {
        assertEquals(listOf("Ada: 95", "Linus: 88"), scoreLines(listOf("Ada", "Linus"), listOf(95, 88)))
    }

    @Test
    fun stopsAtTheShorterList() {
        assertEquals(listOf("Ada: 95"), scoreLines(listOf("Ada", "Linus"), listOf(95)))
    }

    @Test
    fun emptyInputProducesEmptyOutput() {
        assertEquals(emptyList<String>(), scoreLines(emptyList(), listOf(95)))
    }

    private fun scoreLines(names: List<String>, scores: List<Int>): List<String> =
        names.zip(scores).map { (name, score) -> "$name: $score" }
}
