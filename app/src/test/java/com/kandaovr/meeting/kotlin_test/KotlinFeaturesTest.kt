package com.kandaovr.meeting.kotlin_test

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import kotlin.properties.Delegates

// =========================================================================================
// 5. Type Aliases (类型别名) - Helper Definitions
// 将这些定义移到文件顶层，以避免实验性的“嵌套类型别名”功能。
// =========================================================================================

// 必须先定义数据类
private data class UserForAlias(val name: String)

// 再为使用该数据类的函数类型创建别名
private typealias UserClickHandler = (user: UserForAlias, position: Int) -> Unit

/**
 * Kotlin 其他常用特性测试
 */
class KotlinFeaturesTest {

    // =========================================================================================
    // 1. Data Classes (数据类)
    // =========================================================================================

    /**
     * 数据类自动为我们生成了 `equals()`, `hashCode()`, `toString()`, `copy()` 和 `componentN()` 函数。
     * - `copy()`: 可以创建一个对象的副本，并可以选择性地修改某些属性。
     * - `componentN()`: 允许进行解构声明。
     */
    @Test
    fun dataClassTest() {
        data class User(val name: String, val age: Int)

        val user1 = User("Alice", 29)
        val user2 = User("Alice", 29)

        // 1. toString(), equals(), hashCode() 自动生成
        println("Data class toString(): $user1")
        assertEquals(user1, user2) // 值相等，所以 equals() 返回 true

        // 2. copy() 方法
        val olderAlice = user1.copy(age = 30)
        println("Copied and modified user: $olderAlice")
        assertEquals("Alice", olderAlice.name)
        assertNotEquals(user1, olderAlice)

        // 3. 解构声明 (Destructuring Declaration)
        val (name, age) = user1
        println("Destructured: Name is '$name', Age is $age")
        assertEquals("Alice", name)
    }

    // =========================================================================================
    // 2. Sealed Classes (密封类)
    // =========================================================================================

    sealed class UiState {
        object Loading : UiState()
        data class Success(val data: String) : UiState()
        data class Error(val message: String) : UiState()
    }

    /**
     * 密封类用于表示一个受限制的类层次结构。当与 `when` 表达式一起使用时，如果覆盖了所有情况，就不需要 `else` 分支，
     * 这使得代码在处理状态等场景时更加安全。
     */
    @Test
    fun sealedClassTest() {
        fun handleState(state: UiState): String {
            return when (state) {
                is UiState.Loading -> "Showing a loading spinner..."
                is UiState.Success -> "Data loaded: ${state.data}"
                is UiState.Error -> "Error occurred: ${state.message}"
            }
        }

        val successMessage = handleState(UiState.Success("My Awesome Data"))
        val errorMessage = handleState(UiState.Error("Network timeout"))

        println(successMessage)
        println(errorMessage)

        assertEquals("Data loaded: My Awesome Data", successMessage)
    }

    // =========================================================================================
    // 3. Delegated Properties (委托属性)
    // =========================================================================================

    /**
     * 委托属性允许我们将属性的 getter/setter 逻辑委托给另一个对象。
     * - `by lazy`: **懒加载**。属性的值只在第一次被访问时才会计算，非常适合初始化开销大的对象。
     * - `by Delegates.observable`: **可观察属性**。当属性值发生变化时，会触发一个回调。
     */
    @Test
    fun delegatedPropertiesTest() {
        // 1. by lazy
        val heavyObject: String by lazy {
            println("<<< Initializing heavy object >>>") // 这行只会在第一次访问时打印
            "I am a very heavy object"
        }

        println("Lazy delegate: First access...")
        println("Lazy delegate: Value is '$heavyObject'")
        println("Lazy delegate: Second access...")
        println("Lazy delegate: Value is '$heavyObject'") // 不会再次初始化

        // 2. by Delegates.observable
        var observedName: String by Delegates.observable("<no name>") { prop, oldValue, newValue ->
            println("Observable delegate: Property '${prop.name}' changed from '$oldValue' to '$newValue'")
        }

        observedName = "Alice"
        observedName = "Bob"
    }

    // =========================================================================================
    // 4. Object & Companion Object
    // =========================================================================================

    object SingletonManager {
        fun doSomething() = "Doing something in a singleton!"
    }

    class MyClass {
        // companion object 内部的成员类似于 Java 的静态成员。
        // 一个类只能有一个伴生对象。
        companion object {
            const val CONSTANT_VALUE = "I am a compile-time constant."

            fun create(): MyClass = MyClass()
        }
    }

    @Test
    fun objectAndCompanionTest() {
        // 1. object (单例)
        val instance1 = SingletonManager
        val instance2 = SingletonManager
        println("Singleton instance: ${instance1.doSomething()}")
        assertEquals(instance1, instance2)

        // 2. companion object (伴生对象)
        println("Companion object constant: ${MyClass.CONSTANT_VALUE}")
        val myClassInstance = MyClass.create() // 像调用静态方法一样
    }

    // =========================================================================================
    // 5. Type Aliases (类型别名)
    // =========================================================================================

    private fun setOnUserClickListener(listener: UserClickHandler) {
        // 模拟点击事件
        val clickedUser = UserForAlias("ClickedUser")
        val clickedPosition = 3
        listener(clickedUser, clickedPosition)
    }

    @Test
    fun typeAliasTest() {
        var result = ""
        // 使用类型别名后，lambda 签名变得非常清晰
        setOnUserClickListener { user, position ->
            result = "Clicked on ${user.name} at position $position"
            println(result)
        }
        assertEquals("Clicked on ClickedUser at position 3", result)
    }
}