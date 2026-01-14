package com.kandaovr.meeting.kotlin_test

import org.junit.Test

class CollectionsTest {

    @Test
    fun run() {
        println("hello")


        // List
        val lists = listOf<Int>(1, 2, 4, 54)

        println(lists.asReversed())
        println(lists.sorted())

        val mList: MutableList<String> = mutableListOf("sd", "sd", "shkshk")
        println(mList)
    }


}