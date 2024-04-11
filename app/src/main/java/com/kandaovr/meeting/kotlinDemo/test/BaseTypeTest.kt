package com.kandaovr.meeting.kotlinDemo.test

import java.util.*
import kotlin.random.Random


fun main(args: Array<String>) {
    basicTypes()
    ifelse()
    switchCase()
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
    var name: String? = if (int == 0) null else "caicai";
    var uName = name?.toUpperCase(Locale.ROOT)
    println("${uName == null}")
    println("${uName},${name}")
    if (name != null) {
        // 判非空-不再用？修饰
        println("name length:${name.length}")
    }
}

fun switchCase() {
    println("-----------------------------------")
    val count = Random.nextInt(1, 11)
    println("random = $count")
    var str = when (count) {
        0 -> "todo 0"
        1 -> "todo 1"
        2, 3, 4, 5 -> "2..5"
        else -> "todo else"
    }
    println(str)
}

private fun ifelse() {
//    val scanner = Scanner(System.`in`)
//    println("请输入类型 0..3")
    var userInput = 0;
//    if (scanner.hasNextInt()) {
//        userInput = scanner.nextInt()
//    } else {
//        userInput = -1;
//    }

    // if else 省略变量定义
    val str = if (userInput == -1) {
        "输入的不是Int数"
    } else if (userInput == 0) {
        "0为默认指"
    } else if (userInput == 1) {
        "type $userInput"
    } else if (userInput == 2) {
        "$userInput"
    } else {
        "other type"
    }
    println("result--$str")


    // 替换三目运算符
    val result = if (str.equals("2")) 2 else 0
    println("result---$result")


    // kotlin中三元运算符
    val condition = false
    val int = condition.let { if (it) 1 else 0 }
    val int2 = condition.run { if (!this) 0 else 1 }
    println("三目：${int},${int2}")

}