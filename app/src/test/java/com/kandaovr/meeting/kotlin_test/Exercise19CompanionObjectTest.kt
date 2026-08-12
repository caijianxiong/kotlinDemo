package com.kandaovr.meeting.kotlin_test

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 练习 19：使用 companion object 提供命名工厂。
 *
 * 目标：只实现 [ApiConfig.local]。
 * 行为契约：本地开发配置的 baseUrl 为 "http://10.0.2.2:8080"，timeoutSeconds 为 5；每次调用都应得到符合该配置的独立值对象。
 * 约束：不要改动测试；在 [ApiConfig] 的 companion object 中实现命名工厂，而不是在测试中散落默认值。
 */
class Exercise19CompanionObjectTest {

    @Test
    fun localFactoryProvidesTheAndroidEmulatorBaseUrl() {
        assertEquals("http://10.0.2.2:8080", ApiConfig.local().baseUrl)
    }

    @Test
    fun localFactoryProvidesShortDevelopmentTimeout() {
        assertEquals(5, ApiConfig.local().timeoutSeconds)
    }

    @Test
    fun localFactoryReturnsEquivalentValueObjects() {
        assertEquals(ApiConfig.local(), ApiConfig.local())
    }

    private data class ApiConfig(val baseUrl: String, val timeoutSeconds: Int) {
        companion object {
            fun local(): ApiConfig = TODO("Implement the exercise")
        }
    }
}
