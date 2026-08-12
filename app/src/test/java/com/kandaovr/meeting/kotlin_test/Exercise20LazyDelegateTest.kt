package com.kandaovr.meeting.kotlin_test

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 练习 20：使用 lazy 委托延迟计算。
 *
 * 目标：只实现 [AccessToken.preview]。
 * 行为契约：创建对象时不生成预览；首次访问 preview 时生成 raw 的前四个字符并将 buildCount 加一；之后读取复用首次结果且不再次增加计数。
 * 约束：不要改动测试；使用 lazy 委托，不要在 init 块或构造时预先计算。
 */
class Exercise20LazyDelegateTest {

    @Test
    fun previewIsNotBuiltDuringConstruction() {
        val token = AccessToken("abcdef")

        assertEquals(0, token.buildCount)
    }

    @Test
    fun firstPreviewAccessBuildsThePreviewOnce() {
        val token = AccessToken("abcdef")

        assertEquals("abcd", token.preview)
        assertEquals(1, token.buildCount)
    }

    @Test
    fun laterPreviewAccessReusesTheCachedValue() {
        val token = AccessToken("abcdef")

        token.preview
        assertEquals("abcd", token.preview)
        assertEquals(1, token.buildCount)
    }

    private class AccessToken(private val raw: String) {
        var buildCount: Int = 0
            private set

        val preview: String = TODO("Implement the exercise")
    }
}
