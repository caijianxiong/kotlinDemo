package com.kandaovr.meeting.kotlinDemo.test

import java.io.File


fun getMessage(message: String, pre: String = "pre"): String {
    return "[$pre] $message"
}


fun main() {
    usageNotNull()
    println(getMessage(pre = "jajaja", message = "老王"))
    println(getMessage("老王"))
}


fun usageNotNull() {
    var files = File("Test").listFiles()
//    files = arrayOf(
//        File("file1.txt"),
//        File("file2.txt"),
//        File("file3.txt")
//    )
    println(files?.size) // 如果 files 不是 null，那么输出其大小（size）
    val size = files?.size?.times(2)

    println(size)

    val fileSize = files?.size ?: run {
        2 * 2
    }.let {
        it + 1
    }
    println(fileSize)
    // 非空执行代码
    files?.let {
        println("not null run")
    }
    files?.run { println("not null run") }

}