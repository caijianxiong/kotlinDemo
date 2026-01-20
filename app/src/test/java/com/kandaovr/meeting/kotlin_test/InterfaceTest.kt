package com.kandaovr.meeting.kotlin_test

import org.junit.Test


/**
 * 继承接口，解决相同方法，覆盖冲突
 */
class TaxiCar() : ICar, ITrain {
    // 抽象属性:实现类必须赋值
    override val wheelCount: Int
        get() = 4

    /**
     *类似与组合模式---对 TaxiCar 这个“组合对象”的单一调用，被分发到了它所包含的多个“部分”上
     *
     * 因为 ICar 和 ITrain 都有 drive() 方法，所以 TaxiCar 必须提供一个 override 实现来消除歧义。
     * TaxiCar().drive() 调用的就是下面这个方法。
     */
    override fun drive() {
        println("TaxiCar is driving. It needs to decide which parent's method to call.")
        // 使用 super<InterfaceName>.methodName() 来明确指定调用哪个父接口的默认实现

        super<ICar>.drive()
        super<ITrain>.drive()
    }

    fun whoDrive() {
        println("whoDrive is calling this class's own drive() method.")
        drive() // 这里调用的也是上面 TaxiCar 自己重写的 drive()
    }

}

// 抽象属性:实现类必须赋值
class TruckCar(override val wheelCount: Int) : ICar {
    override fun drive() {
        println("TruckCar is driving with $wheelCount wheels.")
    }

}

interface ICar {
    val wheelCount: Int// 抽象属性

    // 为 drive() 添加一个默认实现
    fun drive() {
        println("Driving a car...")
    }
}

interface ITrain {
    // 为 drive() 添加一个默认实现
    fun drive() {
        println("Driving a train...")
    }
}

/*函数式接口（SAM）*/

fun interface IntPredicate {
    fun accept(i: Int): Boolean
}

// 定义函数式接口（仅一个抽象方法：处理请求结果）
fun interface HttpCallback<T> {
    fun onResult(data: T?, error: String?)
}

// 模拟网络请求方法，接收回调接口作为参数
fun requestUserData(userId: String, callback: HttpCallback<String>) {
    // 模拟网络请求耗时
    Thread {
        Thread.sleep(1000)
        // 模拟请求结果
        val result = if (userId == "1001") "用户：张三，年龄：28" else null
        val error = if (userId == "1001") null else "用户不存在"
        // 触发回调
        callback.onResult(result, error)
    }.start()
}

class InterfaceTest {

    @Test
    fun main() {
        println("--- Calling drive on TaxiCar ---")
        TaxiCar().drive()

        println("\n--- Calling whoDrive on TaxiCar ---")
        TaxiCar().whoDrive()
    }

    @Test
    fun testSam(){
        // 没有函数式接口时，实现单抽象方法接口必须创建匿名内部类
        val oldEven = object : IntPredicate {
            override fun accept(i: Int): Boolean {
                return i % 2 == 0
            }
        }

        // 有了函数式接口，可直接用 lambda 表达式替代，大幅减少模板代码，提升可读性和开发效率
        val isEven = IntPredicate { it % 2 == 0 }
        println("Is 7 even? - ${isEven.accept(7)}")

        requestUserData("ssss") { data, error ->
            println("data:$data,error:$error")
        }
    }

}



