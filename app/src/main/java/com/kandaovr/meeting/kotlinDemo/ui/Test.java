package com.kandaovr.meeting.kotlinDemo.ui;

import android.util.Log;

public class Test implements Book{
    @Override
    public void read() {

    }
}


interface Book {
    void read();

    //Java 9 及以后：支持private static方法实现（仅接口内部调用），但普通实例方法仍无私有实现。
    static void get() {
        System.out.println("get");
    }

    default void set(){
        System.out.println("set");
    }
}