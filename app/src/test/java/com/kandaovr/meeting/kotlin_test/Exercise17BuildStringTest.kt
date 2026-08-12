package com.kandaovr.meeting.kotlin_test

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 练习 17：使用 buildString 构建条件文本。
 *
 * 目标：只实现 [searchSummary]。
 * 行为契约：结果始终以 "query=<query>; page=<page>" 开头；filters 非空时追加 "; filters=" 和以 ", " 分隔的过滤条件；保留条件原有顺序。
 * 约束：不要改动测试；使用 buildString 组织按条件追加的文本。
 */
class Exercise17BuildStringTest {

    @Test
    fun includesFiltersWhenTheyExist() {
        assertEquals(
            "query=kotlin; page=2; filters=Android, Flow",
            searchSummary("kotlin", page = 2, filters = listOf("Android", "Flow"))
        )
    }

    @Test
    fun omitsFilterSectionWhenFiltersAreEmpty() {
        assertEquals("query=kotlin; page=1", searchSummary("kotlin", page = 1, filters = emptyList()))
    }

    @Test
    fun keepsAOneItemFilterWithoutExtraSeparator() {
        assertEquals("query=coroutines; page=3; filters=Beginner", searchSummary("coroutines", 3, listOf("Beginner")))
    }

    private fun searchSummary(query: String, page: Int, filters: List<String>): String =
        buildString { append("query=$query; page=$page").append("{$filters.al}") }
}
