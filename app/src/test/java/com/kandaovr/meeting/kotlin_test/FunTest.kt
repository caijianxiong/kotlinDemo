package com.kandaovr.meeting.kotlin_test

import com.kandaovr.meeting.kotlinDemo.listener.Listener
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Kotlin 函数用法测试大全
 * 这个类包含了从基础到高级的各种函数用法，每个方法都是一个可独立运行的测试用例。
 */
class FunTest {

    // =========================================================================================
    // 基础函数用法 (Basic Functions)
    // =========================================================================================

    /**
     * 演示了函数最基本的定义方式、单表达式函数的简写，以及如何使用具名参数和默认参数值。
     */
    @Test
    fun basicFunctionTest() {
        // 1. 标准函数调用
        fun standardSum(a: Int, b: Int): Int {
            return a + b
        }
        assertEquals(5, standardSum(2, 3))

        // 2. 单表达式函数 (Single-Expression function)，返回值类型可被推断
        fun simpleSum(a: Int, b: Int) = a + b
        assertEquals(5, simpleSum(2, 3))

        // 3. 具名参数 (Named Arguments)，可以不按顺序传递参数
        fun printDetails(name: String, age: Int, city: String) {
            println("Name: $name, Age: $age, City: $city")
        }
        printDetails(city = "New York", name = "John", age = 30)

        // 4. 默认参数 (Default Arguments)
        fun greet(name: String, message: String = "Hello") {
            println("$message, $name!")
        }
        greet("Alice") // 使用默认 message
        greet("Bob", "Hi") // 覆盖默认 message
    }

    /**
     * 演示了如何定义和使用可变数量参数 (vararg)。
     */
    @Test
    fun varargTest() {
        fun printAll(vararg messages: String) {
            println("--- Printing messages ---")
            for (msg in messages) println(msg)
        }

        printAll("Hello", "World")
        printAll("Single message")

        // 使用伸展操作符 (*) 将一个数组的内容作为独立参数传递
        val messagesArray = arrayOf("How", "are", "you?")
        printAll("Greeting:", *messagesArray)
    }

    /**
     * 演示了如何为现有类添加新函数，而无需继承它。
     * 这是 Kotlin 一个非常强大的特性。
     */
    @Test
    fun extensionFunctionsTest() {
        // 为 String 类添加一个名为 shout 的扩展函数
        fun String.shout() = "$this!!!"

        val message = "Hello Kotlin"
        println(message.shout()) // 调用扩展函数
        assertEquals("Hello Kotlin!!!", message.shout())
    }

    /**
     * 演示了中缀函数 (infix) 的用法，它允许更自然、类似英语的函数调用语法。
     * 中缀函数必须是成员函数或扩展函数，且只有一个参数。
     */
    @Test
    fun infixFunctionTest() {
        infix fun Int.plus(other: Int): Int {
            return this + other
        }

        // 标准调用
        val result1 = 10.plus(5)
        // 中缀调用
        val result2 = 10 plus 5

        println("Infix result: $result2")
        assertEquals(15, result2)
        assertEquals(result1, result2)
    }


    // =========================================================================================
    // 高阶函数与 Lambda 表达式 (Higher-Order Functions & Lambdas)
    // =========================================================================================

    /**
     * **高阶函数**: 一个函数如果接受函数作为参数，或者返回一个函数，那么它就是高阶函数。
     * **Lambda表达式**: 一种用于创建匿名函数（即没有名字的函数）的简洁语法。
     */
    @Test
    fun higherOrderFunctionAndLambdaTest() {
        // 定义一个高阶函数，它接受一个 Int 和一个函数作为参数
        fun calculate(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
            return operation(a, b)
        }

        // 1. 使用 Lambda 表达式作为参数
        val sum = calculate(10, 5) { x, y -> x + y }
        println("Higher-Order (Sum): $sum")
        assertEquals(15, sum)

        val product = calculate(10, 5) { x, y -> x * y }
        println("Higher-Order (Product): $product")
        assertEquals(50, product)

        // 2. 将函数引用作为参数传递
        fun subtract(a: Int, b: Int) = a - b
        val difference = calculate(10, 5, ::subtract)
        println("Higher-Order (Subtract with reference): $difference")
        assertEquals(5, difference)
    }

    /**
     * 演示了 Lambda 表达式的几种常见语法。
     */
    @Test
    fun lambdaSyntaxTest() {
        val numbers = listOf(1, 2, 3, 4, 5)

        // 1. 完整语法
        numbers.forEach({ number: Int -> println(number) })

        // 2. 如果 lambda 是最后一个参数，可以移到括号外
        numbers.forEach() { number: Int -> println(number) }

        // 3. 如果函数只有一个 lambda 参数，可以省略括号
        numbers.forEach { number: Int -> println(number) }

        // 4. 如果 lambda 的参数类型可以被推断，可以省略
        numbers.forEach { number -> println(number) }

        // 5. 如果 lambda 只有一个参数，可以用 `it` 隐式引用它
        println("--- Using 'it' ---")
        numbers.forEach { println(it) }
    }

    /**
     * 演示了闭包 (Closure) 的概念：Lambda 可以访问并修改其外部作用域的变量。
     */
    @Test
    fun closureTest() {
        var sum = 0
        val numbers = listOf(1, 2, 3, 4, 5)

        // 这个 lambda 访问并修改了外部变量 `sum`，这就是一个闭包。
        numbers.forEach { sum += it }

        println("Closure sum: $sum")
        assertEquals(15, sum)
    }

    /**
     * 演示了接口回调的两种写法：传统的匿名对象和 Kotlin 简洁的 SAM 转换。
     * **SAM (Single Abstract Method) 转换**: 如果一个 Java 接口只有一个抽象方法，Kotlin 允许你用一个 Lambda 表达式来直接实现它。
     */
    @Test
    fun listenerAndCallbackTest() {
        var receivedMessage = ""

        // --- 1. Java 风格的匿名内部类实现 --- 
        // 假设 Listener 是一个 Java 接口
        val javaStyleListener = object : Listener {
            override fun onCallBackListener(str: String) {
                receivedMessage = "Java Style: $str"
            }
        }
        javaStyleListener.onCallBackListener("Hello")
        println(receivedMessage)
        assertEquals("Java Style: Hello", receivedMessage)

        // --- 2. Kotlin SAM 转换的简洁写法 ---
        // fun setListener(listener: Listener) { ... }
        // setListener { message -> 
        //     receivedMessage = "Kotlin Style: $message"
        // }

        // --- 3. 使用函数类型作为回调 --- (这是更 Kotlin-idiomatic 的方式)
        var onCallback: ((String) -> Unit)? = null
        // 设置回调
        onCallback = { message ->
            receivedMessage = "Function Type: $message"
        }
        // 触发回调
        onCallback?.invoke("World")
        println(receivedMessage)
        assertEquals("Function Type: World", receivedMessage)
    }

    @Test
    fun inlineFunTest() {

        val result1 = add(1, 2)
        val result2 = add("1", "2")
        println("result1:$result1, result2:$result2")
    }


    inline fun <reified T> add(a: T, b: T): T {
        return when (a) {
            is Int -> {
                (a + b as Int) as T
            }

            is String -> {
                (a + b) as T
            }

            else -> {
                // 替代过时的 newInstance()，使用 Kotlin 安全实例化方式
                try {
                    // 获取无参构造函数并实例化
                    T::class.java.getConstructor().newInstance()
                } catch (e: Exception) {
                    throw IllegalArgumentException(
                        "不支持的类型 ${T::class.simpleName}，且该类型无公开无参构造函数",
                        e
                    )
                }
            }
        }
    }
}



