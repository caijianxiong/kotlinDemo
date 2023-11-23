package com.kandaovr.meeting.kotlinDemo.data

open class Car(val wheels: List<Wheel>) {

    // wheelCount 设置set方法私有，外部不可访问
    var wheelCount: Int = wheels.size
        private set

    class Wheel(var index: Int)

}