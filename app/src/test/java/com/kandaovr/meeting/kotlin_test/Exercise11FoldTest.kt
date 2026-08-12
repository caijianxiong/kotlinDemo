package com.kandaovr.meeting.kotlin_test

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 练习 11：使用 fold 累积集合结果。
 *
 * 目标：只实现 [cartTotal]。
 *
 * 行为契约：每个商品的金额为 price 乘 quantity，返回所有商品金额之和；空购物车返回 0。
 * 约束：不要改动测试；使用 fold 明确初始值和累积过程。
 */
class Exercise11FoldTest {

    @Test
    fun multipleLinesAreAddedTogether() {
        val lines = listOf(CartLine(price = 10, quantity = 2), CartLine(price = 5, quantity = 3))

        assertEquals(35, cartTotal(lines))
    }

    @Test
    fun oneLineUsesPriceTimesQuantity() {
        assertEquals(28, cartTotal(listOf(CartLine(price = 7, quantity = 4))))
    }

    @Test
    fun emptyCartHasZeroTotal() {
        assertEquals(0, cartTotal(emptyList()))
    }

    private fun cartTotal(lines: List<CartLine>): Int =
        lines.fold(0, { total, line -> total + line.price * line.quantity })


    private data class CartLine(val price: Int, val quantity: Int)
}
