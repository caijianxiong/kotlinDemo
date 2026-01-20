package com.kandaovr.meeting.kotlin_test

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Kotlin 类的用法大全
 * 这个类展示了从基础继承到各种高级特性的用法。
 */
class ClassTest {

    // =========================================================================================
    // 1. 密封类 (Sealed Classes)
    // =========================================================================================
    sealed class Result {
        data class Success(val data: Any?) : Result()
        data class Error(val errorMsg: String) : Result()
        data class Failed(val reason: String) : Result()
        object Loading : Result() // 可以包含 object
    }

    /**
     * 密封类用于表示一个受限制的、可枚举的类层次结构。
     * 当与 `when` 表达式结合使用时，如果覆盖了所有可能的子类，就不需要 `else` 分支，
     * 这使得代码在处理状态、结果等场景时更加类型安全。
     */
    @Test
    fun sealedClassTest() {
        fun handleResult(result: Result): String {
            return when (result) {
                is Result.Error -> "Error: ${result.errorMsg}"
                is Result.Failed -> "Failed: ${result.reason}"
                is Result.Success -> "Success with data: ${result.data}"
                is Result.Loading -> "State is Loading..."
            }
        }

        val successResult = handleResult(Result.Success("User Data"))
        println(successResult)
        assertEquals("Success with data: User Data", successResult)
    }

    // =========================================================================================
    // 2. 嵌套类 vs 内部类 (Nested vs Inner Classes)
    // =========================================================================================
    class Outer {
        private val outerProperty = "I am in the Outer class"

        // 嵌套类 (Nested Class)
        // 不持有外部类的引用，不能访问外部类的成员。就像一个静态内部类。
        class Nested {
            fun getInfo() = "I am a Nested class"
            // fun getOuterProperty() = outerProperty // 编译错误！
        }

        // 内部类 (Inner Class)
        // 持有外部类的引用，可以访问外部类的成员。
        inner class Inner {
            fun getInfo() = "I am an Inner class"
            fun getOuterProperty() = outerProperty // OK!
        }
    }

    @Test
    fun nestedAndInnerClassTest() {
        val nested = Outer.Nested()
        println(nested.getInfo())

        val inner = Outer().Inner() // 创建内部类需要先有外部类的实例
        println(inner.getInfo())
        println("Inner class can access: ${inner.getOuterProperty()}")
        assertEquals("I am in the Outer class", inner.getOuterProperty())
    }

    // =========================================================================================
    // 3. 类委托 (Class Delegation)
    // =========================================================================================

    interface SoundMaker {
        fun makeSound(): String
    }
    class Cat : SoundMaker {
        override fun makeSound() = "Meow!"
    }

    // PetRobot 类将 SoundMaker 接口的实现委托给了传入的 `soundDelegate` 对象。
    // PetRobot 无需自己实现 makeSound()，编译器会自动生成委托调用。
    class PetRobot(soundDelegate: SoundMaker) : SoundMaker by soundDelegate

    /**
     * 类委托是一种强大的设计模式，可以避免脆弱的基类问题，是组合优于继承原则的绝佳实践。
     */
    @Test
    fun classDelegationTest() {
        val catSound = Cat()
        val robotCat = PetRobot(catSound)

        // robotCat 的 makeSound() 调用被委托给了 catSound
        val sound = robotCat.makeSound()
        println("The robot cat says: '$sound'")
        assertEquals("Meow!", sound)
    }

    // =========================================================================================
    // 4. 对象表达式 (Anonymous Classes)
    // =========================================================================================
    interface ClickListener {
        fun onClick()
    }

    /**
     * 对象表达式用于创建一个临时的、一次性的匿名类的实例，通常用于实现接口或继承类。
     */
    @Test
    fun objectExpressionTest() {
        var clicked = false
        // 创建一个 ClickListener 接口的匿名实现
        val listener = object : ClickListener {
            override fun onClick() {
                println("Button was clicked!")
                clicked = true
            }
        }

        listener.onClick()
        assertEquals(true, clicked)
    }

    // =========================================================================================
    // 5. 泛型约束 (Generic Constraints)
    // =========================================================================================

    // `where` 子句可以为一个泛型参数 T 设置多个约束条件
    // 这里要求 T 既是 CharSequence 也是 Comparable<T>
    fun <T> firstAndLastAreEqual(list: List<T>): Boolean where T : CharSequence, T : Comparable<T> {
        if (list.size < 2) return true
        return list.first() == list.last()
    }

    /**
     * 泛型约束允许我们为泛型类型参数指定“能力”，例如它必须实现哪些接口或继承自哪个类。
     */
    @Test
    fun genericConstraintsTest() {
        val stringList = listOf("apple", "banana", "apple")
        val intList = listOf(1, 2, 3) // Int 不是 CharSequence，无法调用

        val result = firstAndLastAreEqual(stringList)
        println("First and last are equal in $stringList: $result")
        assertEquals(true, result)

        // firstAndLastAreEqual(intList) // 编译错误！
    }


    // =========================================================================================
    // 原有代码的修复和保留
    // =========================================================================================
    open class Dog(var name: String) {
        open fun getDogName(): String {
            return name
        }
    }

    open class DogTwo(var name: String, var age: Int) {
        open fun getDogName(): String {
            return name
        }
    }

    // 单参数继承
    class WhiteDog : Dog("white dog") {}

    // 多个参数继承 (已修复构造函数调用语法)
    class BlackDog(name: String, color: Int) : DogTwo(name, 18) {}

}
