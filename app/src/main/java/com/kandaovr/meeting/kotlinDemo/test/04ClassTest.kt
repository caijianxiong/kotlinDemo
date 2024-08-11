package com.kandaovr.meeting.kotlinDemo.test

open class Dog(var name: String) {
    open fun getName(): String {
        return name
    }
}

open class DogTwo(var name: String, var age: Int) {
    open fun getName(): String {
        return name
    }
}

// 单参数继承
class WhiteDog : Dog("white dog") {
    override fun getName(): String {
        return super.getName()
    }
}

// 多个参数继承
class BlackDog(name: String, color: Int) : DogTwo(name = name, 18) {
    override fun getName(): String {
        return super.getName()
    }
}

class Container<T>(vararg em: T) {
    private var elements = em.toMutableList()
    fun add(em: T) {
        elements.add(em)
    }

    fun get(index: Int): T {
        return elements[index]
    }
}


fun main() {

}