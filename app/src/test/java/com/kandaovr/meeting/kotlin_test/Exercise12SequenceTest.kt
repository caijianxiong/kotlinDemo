package com.kandaovr.meeting.kotlin_test

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 练习 12：惰性 Sequence 管道。
 *
 * 目标：只实现 [firstEvenSquares]。
 *
 * 行为契约：按原顺序选择偶数，计算平方，最多返回 count 个结果；count 小于等于 0 时返回空列表。
 * 约束：不要改动测试；使用 asSequence 构建惰性处理管道，最后再生成 List。
 */
class Exercise12SequenceTest {

    @Test
    fun returnsTheRequestedNumberOfEvenSquares() {
        assertEquals(listOf(4, 16, 36), firstEvenSquares(listOf(1, 2, 3, 4, 6, 8), count = 3))
    }

    @Test
    fun keepsOriginalOrderBeforeSquaring() {
        assertEquals(listOf(64, 4), firstEvenSquares(listOf(8, 2, 4), count = 2))
    }

    @Test
    fun nonPositiveCountProducesEmptyList() {
        assertEquals(emptyList<Int>(), firstEvenSquares(listOf(2, 4), count = 0))
    }

    private fun firstEvenSquares(numbers: List<Int>, count: Int): List<Int> =
        numbers.asSequence().filter { it % 2 == 0 }.map { it * it }.take(count).toList()
}
