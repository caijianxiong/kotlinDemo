package com.kandaovr.meeting.kotlin_test

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 练习 03：集合的筛选与映射。
 *
 * 目标：只实现 [pendingTitles]。
 *
 * 行为契约：保留未完成任务，按原顺序返回它们的 title；空输入或没有未完成任务时返回空列表。
 * 约束：不要改动测试；使用集合操作表达“筛选后转换”的过程。
 */
class Exercise03FilterMapTest {

    @Test
    fun pendingTasksKeepTheirOriginalOrder() {
        val tasks = listOf(
            Task("write tests", completed = false),
            Task("release", completed = true),
            Task("review code", completed = false)
        )

        assertEquals(listOf("write tests", "review code"), pendingTitles(tasks))
    }

    @Test
    fun completedTasksAreExcluded() {
        assertEquals(emptyList<String>(), pendingTitles(listOf(Task("release", completed = true))))
    }

    @Test
    fun emptyInputProducesEmptyOutput() {
        assertEquals(emptyList<String>(), pendingTitles(emptyList()))
    }

    private fun pendingTitles(tasks: List<Task>): List<String> =
        tasks.filter { it.completed.not() }.map { task -> task.title }

    private data class Task(val title: String, val completed: Boolean)
}
