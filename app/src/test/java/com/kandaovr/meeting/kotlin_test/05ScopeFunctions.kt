package com.kandaovr.meeting.kotlin_test

import org.junit.Test

/**
 * 作用域函数测试
 * 将所有函数和类包裹在测试类中，以确保它们能被测试运行器正确识别。
 */
class ScopeFunctionsTest {

    /**
     * 这是一个内部类，其作用域被限定在 ScopeFunctionsTest 内部。
     */
    class People {
        lateinit var name: String
        var age: Int = 0
        lateinit var about: String

        override fun toString(): String {
            return "name:$name,age:$age,about:$about"
        }
    }

    fun printNonNull(str: String?) {
        println("print str:$str")
        str?.let {
            println(it)
        }
    }

    fun printIfBothNonNull(strOne: String?, strTwo: String?): Int? = strOne?.let { firstStr ->
        strTwo?.let {
            firstStr.length + it.length
        }
    }

    // run和let不同在于函数作用域内，对象访问方式不同，let默认 it  run是this
    fun getNullableLength(str: String?) {
        val strLen = str?.run {
            this.length
        }
        println("strLen:$strLen")
    }

    // with
    fun getWithNullableLength(str: String) {
        with(str) {
            println("with str:[$str] length $length")
        }

        val people = People()
        with(people) {
            name = "dsd"
            with(people) {
                age = 18
            }
        }
    }

    fun applyFun() {
        val cai = People()
        val retPeople = cai.apply {
            name = "caicai"
            age = 18
            about = "这是我的名字"
        }.toString()
        println("people:$cai")
        println("people:$retPeople")
    }

    fun alsoFun() {
        val cai = People()
        cai.also {
            it.name = "caijianxiong"
            it.age = 18
            it.about = "ssssss"
        }
    }

    private fun nullPrint() {
        var people: People? = null
        people = People().apply {
            name = "ssss"
            age = 12
            about = "sdssds"
        }
        people.let {
            println(it.toString())
        } ?: run {
            println("people :$people")
        }
    }

    /**
     * =================================================
     *  标准作用域函数使用场景及说明
     * =================================================
     */

    /**
     * 1. let:
     * - 上下文对象: `it`
     * - 返回值: lambda表达式的结果
     * - 主要用途:
     *   a. **处理非空对象**：配合安全调用符 `?.let`，可以对一个非空对象执行代码块。
     *   b. **限定变量作用域**：将一个变量的作用域限制在lambda表达式内。
     */
    fun letUseCase(person: People?) {
        val aboutLength = person?.let {
            println("let: Executing on non-null person: ${it.name}")
            it.about.length // 返回 about 字符串的长度
        }
        println("let: Length of 'about' is $aboutLength (null if person was null)")
    }

    /**
     * 2. run:
     * - 上下文对象: `this`
     * - 返回值: lambda表达式的结果
     * - 主要用途:
     *   a. **对象初始化并计算结果**: 功能与`let`相似，但上下文是`this`，可以像在类内部一样直接访问属性和方法，代码更简洁。
     *   b. **作为表达式运行代码块**: 可以作为一个独立的、不带上下文对象的函数来执行一个代码块并返回结果。
     */
    fun runUseCase() {
        val personDescription: String = People().run {
            name = "run_user"
            age = 25
            about = "I am a developer."
            "run: My name is $name, I'm $age years old." // lambda的最后一行是返回值
        }
        println(personDescription)
    }

    /**
     * 3. with:
     * - 上下文对象: `this`
     * - 返回值: lambda表达式的结果
     * - 注意: `with` **不是扩展函数**，它是一个独立的函数。不建议用于可空对象，因为它会使空检查变得复杂。
     * - 主要用途: 对一个**已知非空**的对象执行一系列操作，代码更简洁。
     */
    fun withUseCase(person: People) {
        val result = with(person) {
            println("with: Configuring person's age: $name")
            age += 1 // 'this'可以省略
            "with: Age is now $age"
        }
        println(result)
        println("with: Person after with block: $person")
    }

    /**
     * 4. apply:
     * - 上下文对象: `this`
     * - 返回值: **上下文对象本身 (`this`)**
     * - 主要用途: **对象配置**。因为它返回对象本身，所以非常适合用于链式调用或类似构建器(Builder)的风格。
     */
    fun applyUseCase(): People {
        return People().apply {
            name = "apply_user"
            age = 1
            about = "Just born."
        }.apply {
            println("apply: Configuring in the first block...")
        }.apply {
            println("apply: And in the second block...")
        }
    }

    /**
     * 5. also:
     * - 上下文对象: `it`
     * - 返回值: **上下文对象本身 (`it`)**
     * - 主要用途:
     *   a. **执行附加操作/副作用**: 用于执行不影响对象本身的操作，如日志记录、数据校验、添加到集合等。
     *   b. 因为返回对象本身，所以可以轻松地嵌入到链式调用中进行调试。
     */
    fun alsoUseCase() {
        val person = People()
            .apply {
                name = "also_user"
                age = 5
                about = "I am in kindergarten."
            }
            .also {
                println("also: Just created a person -> Name: ${it.name}, Age: ${it.age}")
            }
            .also {
                println("also: Added person to a list (simulation).")
            }

        println("also: The final person object is: $person")
    }


    @Test
    fun runAllScopeFunctionExamples(){
        printIfBothNonNull(null, "null")
        printIfBothNonNull("null", null)
        printIfBothNonNull("null", "null02")

        getWithNullableLength("with")

        applyFun()
        println("----------------------------------")

        nullPrint()

        // --- 以下为新增的说明和用例 ---
        println("\n======= 标准作用域函数使用场景及说明 =======")

        val testPerson = People().apply { name = "ScopeTester"; age = 99; about = "A person for testing scopes." }

        println("\n--- 1. let: 安全调用与链式操作 ---")
        letUseCase(testPerson)
        letUseCase(null)

        println("\n--- 2. run: 对象初始化并计算结果 ---")
        runUseCase()

        println("\n--- 3. with: 对已知非空对象进行操作 ---")
        withUseCase(testPerson)

        println("\n--- 4. apply: 对象配置 (Builder-style) ---")
        val appliedPerson = applyUseCase()
        println("Created person with apply: $appliedPerson")

        println("\n--- 5. also: 执行附加操作/副作用 (如日志) ---")
        alsoUseCase()
    }
}