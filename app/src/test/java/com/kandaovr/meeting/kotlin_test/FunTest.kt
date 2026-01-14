package com.kandaovr.meeting.kotlin_test

import com.kandaovr.meeting.kotlinDemo.listener.Listener


// 声明接口变量
private var mListener: Listener? = null

// 定义set接口方法
fun setListener(listener: Listener) {
    mListener = listener
}

// 声明函数类型的成员变量-没有入参，返回String类型
private var onCallbackListener: ((String) -> Unit)? = null

// 定义set接口方法--匿名函数作为入参
fun setListener(listener: ((String) -> Unit)?) {
    onCallbackListener = listener
}


fun main(str: Array<String>) {

    println("funTest result:${funTest(2, "2")}")
    println("匿名函数调用 result:${getNameLength("zhangsan")}")
    println("高级函数 result:${getNameLength("zhangsan") { s -> if (s.length > 5) 30 else s.length }}")

    //高级函数
    advancedFun()
}

/*高阶函数-接口回调*/
private fun advancedFun() {
    // 接口回调一般写法
    setListener(object : Listener {
        override fun onCallBackListener(str: String) {
            println("接口callback:$str")
        }
    })
    // 接口回调kotlin写法  设置接口监听---调用端
    setListener { s ->
        println("callback :$s")
    }
    // 触发接口回调
    mListener?.onCallBackListener("hello word!")
    onCallbackListener?.invoke("kotlin 触发返回参数111")
}


/*函数*/
fun funTest(int: Int, str: String): Boolean {
    return int.toString() == str
}

fun funTestSimple(int: Int, str: String): Boolean = int.toString() == str

// 匿名函数 var funName: (input_param_type)->ReturnType={ input-> "do something return" }
val getNameLength: (String) -> Int = { s ->
    s.length
}

// 高阶函数
fun getNameLength(str: String, sumLength: (String) -> Int): Int {
    return sumLength(str)
}

/**
 * 可变数量参数
 */
fun usageFun(vararg msgs:String){
    for (msg in msgs){

    }
}

/**
 * 扩展函数
 */
fun operatorFun(){
    operator fun Int.times(str: String) = str.repeat(this)       // 1
    println(2 * "Bye ")                                          // 2

    operator fun String.get(range: IntRange) = substring(range)  // 3
    val str = "Always forgive your enemies; nothing annoys them so much."
    println(str[0..14])
}

/*函数*/