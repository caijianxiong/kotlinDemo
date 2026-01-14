package com.kandaovr.meeting.kotlin_test

import java.util.*
import kotlin.random.Random


fun main(args: Array<String>) {
    usageIs()
}

private fun usageScanner() {
    val scanner = Scanner(System.`in`)
    println("请输入类型 0..3")
    var userInput = 0
    while (scanner.hasNextInt()) {
        userInput = scanner.nextInt()
        println(userInput)
    }
}

fun usageWhenElse() {
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

private fun usageIfElse() {
    var userInput = 0
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

fun usageIs() {
    val str = "sdsad"
    var result = str is String
    println("is $result")
}
