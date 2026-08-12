package com.kandaovr.meeting.kotlin_test

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 练习 22：构建并转换 Flow。
 *
 * 目标：只实现 [positiveSquares]。
 * 行为契约：按原始顺序发射所有正数的平方；0 和负数不发射；输入为空时 Flow 不发射任何值。
 * 约束：不要改动测试；将 List 转为 Flow 后再表达筛选和转换，测试通过 toList 收集结果。
 */
class Exercise22FlowTest {

    @Test
    fun emitsSquaresOfPositiveValuesInOrder() = runBlocking {
        assertEquals(listOf(1, 9, 16), positiveSquares(listOf(-2, 0, 1, 3, 4)).toList())
    }

    @Test
    fun emitsNothingWhenNoPositiveValueExists() = runBlocking {
        assertEquals(emptyList<Int>(), positiveSquares(listOf(-3, 0, -1)).toList())
    }

    @Test
    fun emptyInputProducesAnEmptyFlow() = runBlocking {
        assertEquals(emptyList<Int>(), positiveSquares(emptyList()).toList())
    }

    private fun positiveSquares(values: List<Int>): Flow<Int> =
        TODO("Implement the exercise")
}
