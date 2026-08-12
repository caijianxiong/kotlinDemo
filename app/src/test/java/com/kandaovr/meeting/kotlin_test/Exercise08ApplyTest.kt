package com.kandaovr.meeting.kotlin_test

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 练习 08：对象配置与 apply。
 *
 * 目标：只实现 [createSearchRequest]。
 *
 * 行为契约：keyword 去除首尾空白，page 为 1，pageSize 为 20。
 * 约束：不要改动测试；使用 apply 在创建对象时完成配置。
 */
class Exercise08ApplyTest {

    @Test
    fun keywordIsTrimmed() {
        assertEquals("kot lin", createSearchRequest("  kot lin  ").keyword)
    }

    @Test
    fun pageStartsAtOne() {
        assertEquals(1, createSearchRequest("kotlin").page)
    }

    @Test
    fun pageSizeIsTwenty() {
        assertEquals(20, createSearchRequest("kotlin").pageSize)
    }

    private fun createSearchRequest(keyword: String): SearchRequest =
        SearchRequest().apply {
            this.keyword = keyword.trimStart().trimEnd()
            page = 1
            pageSize = 20
        }

    private data class SearchRequest(
        var keyword: String = "",
        var page: Int = 0,
        var pageSize: Int = 0
    )
}
