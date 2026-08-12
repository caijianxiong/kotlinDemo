package com.kandaovr.meeting.kotlin_test

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

/**
 * 练习 18：使用 require 校验调用方参数。
 *
 * 目标：只实现 [createPageRequest]。
 * 行为契约：page 必须大于等于 1，pageSize 必须在 1 到 100 之间（包含边界）；合法输入原样生成请求；非法输入抛出 IllegalArgumentException。
 * 约束：不要改动测试；使用 require 表达函数入口处的参数前置条件。
 */
class Exercise18RequireTest {

    @Test
    fun validBoundaryValuesCreateARequest() {
        assertEquals(PageRequest(page = 1, pageSize = 100), createPageRequest(page = 1, pageSize = 100))
    }

    @Test
    fun pageBelowOneIsRejected() {
        assertThrows(IllegalArgumentException::class.java) {
            createPageRequest(page = 0, pageSize = 20)
        }
    }

    @Test
    fun pageSizeOutsideSupportedRangeIsRejected() {
        assertThrows(IllegalArgumentException::class.java) {
            createPageRequest(page = 1, pageSize = 101)
        }
    }

    private fun createPageRequest(page: Int, pageSize: Int): PageRequest =
        TODO("Implement the exercise")

    private data class PageRequest(val page: Int, val pageSize: Int)
}
