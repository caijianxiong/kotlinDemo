package com.kandaovr.meeting.kotlinDemo

import androidx.core.util.rangeTo
import kotlin.random.Random

// 提前定义，未初始化
lateinit var preDef: String


fun main(args: Array<String>) {


    var n = 123

    var str = "ha";
    var num: Int? = str.toIntOrNull() // ?.变量可为null
    println("${num}-->skdaksk")
    var a = num

    // is 类型判断
    println(a is Int)

    // 区间
    println("区间测试--------------")
    for (i in 1..6) {
        print("$i\t")
    }

    println("\n逆序----------------")
    for (i in 6 downTo 1) {
        print("$i\t")
    }

    println("\n设置步长")
    for (i in 10 downTo 6 step 2) {
        print("$i\t")
    }

    println("\n 使用 until 函数排除结束元素")
    for (i in 1 until 6) {
        print("$i\t")
    }

    println("\n ranges-----------------------")

    val a1 = (1..100).random()
    if (a1 in 1..10) {
        println("a1 is in 10 $a1 ")
    } else {
        println("a1 is not in 10 $a1")
    }


    println("end-----------------")

    println("基本数据类型")

    // == 比较值的大小 === 比较地址是否相同

    var numInt = 12
    var numLong = 454654_1154L
    var numDouble = 0.12
    var numFloat = 12.1212121f

    var result = numInt + numLong + numDouble
    println("result:${result.toLong()}")
    println("int min${Int.MIN_VALUE},max${Int.MAX_VALUE} ")

    println("数组————————————————————————")

    var arrary = arrayOf("hahds", "haha", Book("book", 222), 4, 5, 6)

    println("${arrary[0]} ${arrary[1]}")

    for (i in arrary) {
        print("$i\t")
    }

    println()

    var str03 = """1
        2
        3
        4
    """
    println("多行字符串-$str03")

    var str04 = "1" +
            "2" +
            "3" +
            "4"
    println("普通换行+，---$str04")

    println("if-----------------------------else")
    var c = (0..100).random()
    var b = (21..50).random()
    var maxInt = if (b > c) Book(b.toString(), 1) else Book(c.toString())

    println("b:$b, c:$c ,max:${maxInt.name_base}")

    println("end -----------------------------")




    println("when--》switch 语法——————————————————————————————————————")
    var aaa: Int = (0..10).random()

    println("int value :$aaa")

    when (aaa) {
        0 -> {
            println(aaa)
        }

        1 -> {
            println(aaa)
        }

        3 -> {
            println(aaa)
            println(" end ")
        }

        else -> {
            println("else-->$aaa")
        }
    }

    println("loop start -----")
    aaa@ for (i in 1..100) {
        for (j in 2..60) {
            println("i+j=${i + j}")
            if (i + j > 5) {
                break@aaa //break +标记 跳出标记的for循环，不加标记只跳出当前循环
            }
        }
        println("loop for i $i")
    }
    println("---------------------------------")

    println("test array")
    var ints = intArrayOf(5, 2, 43, 1, 1, 3455, 2)
    var strings = arrayOf("sdd", "sd", "gggg")

    var dobuleay = DoubleArray(2)
    dobuleay[0] = 12.3
    println("array size:${dobuleay.size}")

    ints.forEach {
        print("---- $it")
    }

    println()

    // 伴生对象，实现调用起静态方法
    var result_call = Book.create()


    // 访问嵌套类
    var innerNum = Book.InnerClass().innerNum;
    println("innerNum :$innerNum")

    println("---------------------------------")


    // byte code
    var byteString = "123456"
    var byteArray = byteString.toByteArray(Charsets.UTF_8)
    for (a in byteArray) {
        println("byte:${a.toChar().toString()}")
    }


}







