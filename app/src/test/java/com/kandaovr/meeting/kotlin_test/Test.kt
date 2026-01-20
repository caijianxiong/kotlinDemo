package com.kandaovr.meeting.kotlin_test

import org.junit.Test

class Test {
    class People(var name: String)

    @Test
    fun funtest() {
        var str = People("1111")
        fun change(string: People) {
            string.name = "3333"
        }
        change(str)
        println(str.name)
    }






}
