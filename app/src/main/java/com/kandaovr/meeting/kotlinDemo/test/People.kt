package com.kandaovr.meeting.kotlinDemo.test

public class People {
    lateinit var name: String
    var age: Int = 0
    lateinit var about: String

    override fun toString(): String {
        return "name:$name,age:$age,about:$about"
    }
}