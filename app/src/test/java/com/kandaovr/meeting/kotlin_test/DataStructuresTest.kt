package com.kandaovr.meeting.kotlin_test

import org.junit.Test
import java.util.Collections
import java.util.LinkedHashMap
import java.util.Locale
import java.util.TreeMap
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.iterator

/**
 * 基本数据结构
 * 列表（List）、集合（Set）、映射（Map）、数组（Array）
 */


class DataStructuresTest {

    @Test
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
    }

    @Test
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
        hashMap["Apple"] = 1
        hashMap["Banana"] = 2
        hashMap["Cherry"] = 3
    }

    @Test
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

    @Test
    fun usageArray() {
        println("--usageArray-------------")
        val intArray = arrayOf(1, 2, 3, 4)
        println(intArray.joinToString())  // 输出: 1, 2, 3, 4

        val stringArray = arrayOf("Apple", "Banana", "Cherry")
        println(stringArray.joinToString())  // 输出: Apple, Banana, Cherry
    }

    @Test
    fun advancedDataStructuresTest() {
        println("\n--advancedDataStructuresTest-------------")

        // --- 更多数组类型 ---
        println("\n--- Advanced Array Types ---")

        // 1. 基本类型数组 (IntArray, DoubleArray, etc.)
        // 比 Array<Int> 更高效，因为它避免了对象的装箱拆箱操作。
        val primitiveIntArray = intArrayOf(10, 20, 30)
        println("Primitive IntArray: ${primitiveIntArray.joinToString()}")

        // 2. 使用 lambda 表达式创建数组
        // 创建一个大小为 5 的数组，元素为其索引的平方
        val squares = Array(5) { index -> index * index }
        println("Array created with lambda: ${squares.joinToString()}")

        // --- 更多 Map 实现 ---
        println("\n--- Advanced Map Implementations ---")

        // 1. LinkedHashMap: 保持元素的插入顺序
        val linkedMap = LinkedHashMap<String, Int>()
        linkedMap["three"] = 3
        linkedMap["one"] = 1
        linkedMap["two"] = 2
        println("LinkedHashMap (insertion order): $linkedMap")

        // 2. TreeMap: 根据键的自然顺序进行排序
        val treeMap = TreeMap<String, Int>()
        treeMap["three"] = 3
        treeMap["one"] = 1
        treeMap["two"] = 2
        println("TreeMap (natural key order): $treeMap")

        // --- 线程安全的数据结构 ---
        println("\n--- Thread-Safe Data Structures ---")

        // 1. CopyOnWriteArrayList: 读多写少的场景下非常高效。
        // 每次写入（add, set, remove）时，都会创建一个底层数组的副本。
        // 读操作完全无锁，但写操作成本较高。
        val copyOnWriteList = CopyOnWriteArrayList<String>(listOf("a", "b"))
        // 示例：在迭代时可以安全地修改
        for (item in copyOnWriteList) {
            println("Reading CopyOnWriteArrayList: $item")
            if(copyOnWriteList.size < 4) copyOnWriteList.add("c") // 增加一个限制防止无限循环
        }
        println("Final CopyOnWriteArrayList: $copyOnWriteList")


        // 2. Collections.synchronized...: 将普通集合包装成线程安全的集合。
        // 通过在每个方法上加锁（synchronized）来实现线程安全，所有操作（读和写）都需要获取锁，性能开销较大。
        val normalList = mutableListOf(1, 2, 3)
        val synchronizedList = Collections.synchronizedList(normalList)
        // 在多线程环境中使用 synchronizedList
        // 迭代时需要手动同步
        // synchronized(synchronizedList) {
        //     for (item in synchronizedList) {
        //         ...
        //     }
        // }
        println("Synchronized List created: $synchronizedList")


        // 3. ConcurrentHashMap: 高性能的线程安全 Map。
        // 它使用分段锁（或在现代Java中使用更复杂的技术）来允许多个线程同时读写 Map 的不同部分，性能远超同步的 HashMap。
        val concurrentMap = ConcurrentHashMap<String, Int>()
        concurrentMap["one"] = 1
        val previousValue = concurrentMap.put("one", 11)
        println("ConcurrentHashMap: $concurrentMap, previous value for 'one' was $previousValue")

    }

    @Test
    fun usageIn() {
        var intArray = intArrayOf(1, 2, 3, 4, 6, 64)
        for (i in intArray) {

        }
        var isIn = 6 in (2..9)
        var random = (0..100).random()
        var isInArray = 5 in intArray
        println("isInArray $isInArray")
    }

    @Test
    fun usageFilter() {
        val arrayList = arrayListOf("apple", "sjkdj", "124sada", "adsada")
        arrayList
            .filter { it.startsWith("a") }
            .sortedBy { it }
            .map { it.uppercase(Locale.getDefault()) }
            .onEach {
                println("onEach:$it")
            }
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
    @Test
    fun map() {
        val mixedList = listOf('a', 1, 2.0, "b", 3L) // 混合类型数组
        val bytes = mixedList.filterIsInstance<Number>().map { it.toChar() }
        println(bytes)
    }
}






