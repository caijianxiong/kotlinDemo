package com.kandaovr.meeting.kotlin_test

import java.util.*
import kotlin.random.Random

class BaseTypeTest {

    fun main(args: Array<String>) {
        basicTypes()
        nullable()
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    fun basicTypes() {
        var byte: Byte = 2
        var short: Short = 2
        var int: Int = -2
        var long: Long = 2

        var fl: Float = 2.00222f
        var db: Double = 2.00222
        var bl: Boolean = false
        var ca: Char = 'a'
        /*无符号类型需要依赖 特定的库*/
//    var ul: ULong = 222u;
//    val ui: UInt = 522u
//    println("basicTypes $ul")
    }


    fun nullable() {
        val int = Random.nextInt(0, 2)
        var name: String? = if (int == 0) null else "caicai"
        var uName = name?.uppercase(Locale.ROOT)
        println("${uName == null}")
        println("${uName},${name}")
        if (name != null) {
            // 判非空-不再用？修饰
            println("name length:${name.length}")
        }
    }

    fun inTest() {
        val inIs = 3 in 1..10
        println("inTest $inIs")

        for (a in 1..10){

        }
    }
}
