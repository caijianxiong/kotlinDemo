package com.kandaovr.meeting.kotlinlearn

import android.util.Log

class Book constructor(string: String, int: Int) : Base(), IBase { // 1, 主构造函数  2,继承抽象类 3,实现接口


    // private声明私有构造器
    private constructor(double: Double) : this("", 12) {
        // 次构造函数，必须this(),代理主构造函数
    }

    public constructor(name: String) : this(name, 111)

    private var num: Int = int // 可以init中或者直接使用主构造器中的值
        set(value) {
            field = if (value > 10) value else -1
        }
    var name: String = ""
        get() = field.toUpperCase()

    init {
        num = int;
        name = string
    }

    // 实现抽象类方法
    override fun getClassName(): String {
        Log.i(this.javaClass.simpleName, "getClassName: ")
        return super.getClassName()
    }

    override fun ISetName() {
        TODO("Not yet implemented")
    }



    /**
     * 嵌套类
     */
    class InnerClass constructor() {
        var innerNum: Int = 123456
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