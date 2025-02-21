package com.kandaovr.meeting.kotlinDemo.test

/**
 * 作用域函数
 */

val empty = "test".let {
    println(it)
    it.isEmpty()
}

fun printNonNull(str: String?) {
    println("print str:$str")
    str?.let {
        println(it)
    }
}

fun printIfBothNonNull(strOne: String?, strTwo: String?) {
    val strLen = strOne?.let { firstStr ->
        println("first:$firstStr")
        strTwo?.let { secondStr ->
            println("first:$firstStr,secondStr:$secondStr")
            firstStr.length + secondStr.length
        }
    }
    println("strLen:$strLen")
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

    var people = People()
    with(people) {
        name = "dsd"
        with(people) {
            age = 18
        }
    }
}

class People {
    lateinit var name: String
    var age: Int = 0
    lateinit var about: String

    //    companion object {
//        lateinit var name: String
//        var age: Int = 0
//        lateinit var about: String
//    }
    override fun toString(): String {
        return "name:$name,age:$age,about:$about"
    }
}


fun applyFun() {
    val cai = People()
    var retPeople = cai.apply {
        name = "caicai"
        age = 18
        about = "这是我的名字"
    }.toString()
    println("people:$cai")
    println("people:$retPeople")
}

fun alsoFun() {
    val cai = People()
    var ret = cai.also {
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

fun main() {
    printIfBothNonNull(null, "null")
    printIfBothNonNull("null", null)
    printIfBothNonNull("null", "null02")

    getWithNullableLength("with")

    applyFun()
    println("----------------------------------")

    nullPrint()

}

