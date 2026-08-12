package com.kandaovr.meeting.kotlin_test

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 练习 04：按键分组并汇总。
 *
 * 目标：只实现 [totalByCategory]。
 *
 * 行为契约：按 category 汇总每组 amount；空输入返回空 Map。
 * 约束：不要改动测试；先表达分组关系，再表达每组的汇总值。
 */
class Exercise04GroupingTest {

    @Test
    fun expensesWithSameCategoryAreAddedTogether() {
        val expenses = listOf(
            Expense("food", 12),
            Expense("travel", 30),
            Expense("food", 8)
        )

        assertEquals(mapOf("food" to 20, "travel" to 30), totalByCategory(expenses))
    }

    @Test
    fun oneExpenseCreatesOneCategoryEntry() {
        assertEquals(mapOf("book" to 25), totalByCategory(listOf(Expense("book", 25))))
    }

    @Test
    fun emptyInputProducesEmptyMap() {
        assertEquals(emptyMap<String, Int>(), totalByCategory(emptyList()))
    }

    private fun totalByCategory(expenses: List<Expense>): Map<String, Int> =
        expenses.groupBy { it.category }.mapValues { it.value.sumOf { expense -> expense.amount } }

    private data class Expense(val category: String, val amount: Int)
}
