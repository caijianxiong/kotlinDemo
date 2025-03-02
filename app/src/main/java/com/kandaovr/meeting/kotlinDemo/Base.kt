package com.kandaovr.meeting.kotlinDemo

/**
 * 抽象类
 */
abstract class Base(name: String) : IBook {

    private var num_base = 0

    lateinit var name_base: String

    var pages_base = 100

    init {
        this.name_base = name
        println("Base println: $name")
    }

    open fun getClassName(): String {
        return ""
    }

    open fun f() {
        println(this.javaClass.classes)
    }

    // override加final 不让子类再复写
    final override fun getBookNameA(): String {
        return super.getBookNameA()

    }

    /**
     * c抽象方法
     */
    abstract fun myBaseFun():String


}