package com.kandaovr.meeting.kotlin_test

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 练习 13：数据类的解构声明。
 *
 * 目标：只实现 [coordinateLabel]。
 * 行为契约：将坐标格式化为 "lat=<latitude>, lon=<longitude>"；整数和小数都保持 Kotlin 默认的字符串形式。
 * 约束：不要改动测试；在函数中使用解构声明读取 [Coordinate] 的两个属性。
 */
class Exercise13DestructuringTest {

    @Test
    fun formatsBothCoordinateValues() {
        assertEquals("lat=31.23, lon=121.47", coordinateLabel(Coordinate(31.23, 121.47)))
    }

    @Test
    fun supportsNegativeCoordinateValues() {
        assertEquals("lat=-33.87, lon=151.21", coordinateLabel(Coordinate(-33.87, 151.21)))
    }

    @Test
    fun supportsTheOrigin() {
        assertEquals("lat=0.0, lon=0.0", coordinateLabel(Coordinate(0.0, 0.0)))
    }

    private fun coordinateLabel(coordinate: Coordinate): String =
        "lat=${coordinate.latitude}, lon=${coordinate.longitude}"


    private data class Coordinate(val latitude: Double, val longitude: Double)
}
