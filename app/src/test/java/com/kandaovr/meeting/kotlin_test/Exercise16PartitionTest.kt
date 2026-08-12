package com.kandaovr.meeting.kotlin_test

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 练习 16：使用 partition 将集合分为两组。
 *
 * 目标：只实现 [splitCompleted]。
 * 行为契约：返回的 Pair 第一个列表是已完成任务，第二个列表是未完成任务；每组都保持原始相对顺序；空输入返回两个空列表。
 * 约束：不要改动测试；使用 partition 一次完成二元分组。
 */
class Exercise16PartitionTest {

    @Test
    fun separatesCompletedAndPendingTasks() {
        val done = Task("write tests", completed = true)
        val pending = Task("review", completed = false)
        val secondDone = Task("release", completed = true)

        assertEquals(Pair(listOf(done, secondDone), listOf(pending)), splitCompleted(listOf(done, pending, secondDone)))
    }

    @Test
    fun keepsEveryTaskWhenOneGroupIsEmpty() {
        val pending = listOf(Task("write tests", false), Task("review", false))

        assertEquals(Pair(emptyList<Task>(), pending), splitCompleted(pending))
    }

    @Test
    fun emptyInputProducesTwoEmptyLists() {
        assertEquals(Pair(emptyList<Task>(), emptyList<Task>()), splitCompleted(emptyList()))
    }

    private fun splitCompleted(tasks: List<Task>): Pair<List<Task>, List<Task>> =
        tasks.partition { it.completed }

    private data class Task(val title: String, val completed: Boolean)
}
