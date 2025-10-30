package com.kandaovr.meeting.kotlinDemo.test

import java.util.Locale
import java.util.concurrent.CopyOnWriteArrayList

/**
 * 基本数据结构
 * 列表（List）、集合（Set）、映射（Map）、数组（Array）
 */


fun main() {
//    BaseDataStructuresTest.usageList()
//    BaseDataStructuresTest.usageSet()
//    BaseDataStructuresTest.usageMap()
//    BaseDataStructuresTest.usageArray()
//    BaseDataStructuresTest.usageIn()
//    BaseDataStructuresTest.usageFilter()
    BaseDataStructuresTest.map()
}

object BaseDataStructuresTest {
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

    fun usageFilter() {
        val arrayList = arrayListOf("apple", "sjkdj", "124sada", "adsada")
        arrayList
            .filter { it.startsWith("a") }
            .sortedBy { it }
            .map { it.uppercase(Locale.getDefault()) }
            .forEach { println(it) }

        println("------------------age----------")
        val age = arrayListOf(12, 3, 56, 43, 2, 67, 8)
        age.filter { it > 10 } // true 输出满足条件的数据
            .forEach {
//                println("$it")
            }
        age.filterNot { it > 10 } // 数组不满足条件的数组
            .forEach {
                println("$it")
            }

        val mixedList = listOf("a", 1, 2.0, "b", 3L) // 混合类型数组
        val strings = mixedList.filterIsInstance<String>() // 筛选指定类型的元素数组
        println(strings)

        val nullableList = listOf("a", null, "b", null, "c")
        val nonNullList = nullableList.filterNotNull() //筛选非空元素数组
        println(nonNullList) // 输出：[a, b, c]

    }

    // 转换
    fun map() {
        val mixedList = listOf('a', 1, 2.0, "b", 3L) // 混合类型数组
        val bytes = mixedList.filterIsInstance<Number>().map { it.toChar() }
        println(bytes)
    }
}





