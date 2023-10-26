package com.kandaovr.meeting.kotlinDemo

import android.util.Log
import java.lang.reflect.Array.get

class Book constructor(string: String, int: Int) : Base(string), IBase { // 1, 主构造函数  2,继承抽象类 3,实现接口

    // private声明私有构造器
    private constructor(double: Double) : this("", 12) {
        // 次构造函数，必须this(),代理主构造函数
    }

    public constructor(name: String) : this(name, 111)

    private var num: Int = int // 可以init中或者直接使用主构造器中的值
        set(value) {
            field = if (value > 10) value else -1
        }
    init {
        num = int;
        name_base = string


    }

    // 实现抽象类方法
    override fun getClassName(): String {
        Log.i(this.javaClass.simpleName, "getClassName: ")
        return super.getClassName()
    }

    override fun ISetName() {
        TODO("Not yet implemented")
    }

    override fun f() {
        // 父类包含相同方法，调用父类方法的指名父类
        super<Base>.f()
        super<IBase>.f()
    }

    override fun myBaseFun(): String {
        println("myBaseFun")
        return "";
    }

    /**
     * 伴生对象--静态方法
     */
    companion object Factory {
        fun create(): String = "call static method"
    }

    /**
     * 嵌套类
     */
   open class InnerClass constructor() {
        var innerNum: Int = 123456
        open fun staticMethod():String{
            return "llll"
        }
    }

    /**
     * 嵌套内部类
     */
    inner class InnerCls {

    }


    private lateinit var mListener: Listener
    fun setListener(listener: Listener) {
        mListener = listener
    }

    // 自推导函数返回值
    fun getSum(a: Int, b: Int) = a / 2 + b

    interface Listener {
        fun test()
    }


}