package com.kandaovr.meeting.kotlinDemo.test

import java.util.concurrent.CopyOnWriteArrayList

/**
 * 基本数据结构
 * 列表（List）、集合（Set）、映射（Map）、数组（Array）
 */


fun main() {
    usageList()
    usageSet()
    usageMap()
    usageArray()
    usageIn()
}

fun usageList() {
    println("--usageList-------------")
    val immutableList = listOf<Int>(1, 45, 77, 4, 7987, 45)
    println("不可变列表：$immutableList ")
    var list = immutableList.toList()

    val mutableList = mutableListOf<Int>()
    mutableList.add(2)
    mutableList.addAll(immutableList)
    println("可变列表：$mutableList")
    print("for")
    for (i in mutableList) {
        print("-$i")
    }
    mutableList.sort()
    println()
    println("可变列表sort：${mutableList}")

    // ArrayList
    var arrayList = ArrayList<Int>()
    for (i in 0..6) {
        arrayList.add(i)
    }
    println("arrayList:$arrayList")
    arrayList = arrayListOf()
    for (i in 7..10) {
        arrayList.add(i)
    }
    println("arrayList:$arrayList")

    var copyOnWriteArrayList = CopyOnWriteArrayList<String>()
    copyOnWriteArrayList.add("123")
}

fun usageSet() {
    println("--usageSet-------------")
    val immutableSet = setOf("Apple", "Banana", "Cherry")
    println("不可变set:$immutableSet")
    var result = immutableSet.any { it == "Apple" }
    println("result:$result")

    val mutableSet = mutableSetOf("Apple", "Banana", "Cherry")
    mutableSet.add("Date")
    println("可变set:$mutableSet")

    val hashMap = HashMap<String, Int>()
}

fun usageMap() {
    println("--usageMap-------------")
    val immutableMap = mapOf("Apple" to 1, "Banana" to 2, "Cherry" to 3)
    println(immutableMap)  // 输出: {Apple=1, Banana=2, Cherry=3}

    val mutableMap = mutableMapOf("Apple" to 1, "Banana" to 2, "Cherry" to 3)
    mutableMap["Date"] = 4
    println(mutableMap)  // 输出: {Apple=1, Banana=2, Cherry=3, Date=4}

    // 遍历map key values
    for ((k, v) in immutableMap) {
        println("$k -> $v")
    }

}

fun usageArray() {
    println("--usageArray-------------")
    val intArray = arrayOf(1, 2, 3, 4)
    println(intArray.joinToString())  // 输出: 1, 2, 3, 4

    val stringArray = arrayOf("Apple", "Banana", "Cherry")
    println(stringArray.joinToString())  // 输出: Apple, Banana, Cherry
}

fun usageIn() {
    var intArray = intArrayOf(1, 2, 3, 4, 6, 64)
    for (i in intArray) {

    }
    var isIn = 6 in (2..9)
    var random = (0..100).random()
    var isInArray = 5 in intArray
    println("isInArray $isInArray")
}



