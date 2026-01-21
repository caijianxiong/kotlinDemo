package com.kandaovr.meeting.kotlinDemo.jni

import android.util.Log

/**
 * 一个实现了具体回调逻辑的类。
 * 我们将把这个类的实例传递给 C++ 层，并由 C++ 持有和调用。
 */
class JniCallbackHandler {

    companion object {
        private const val TAG = "JniCallbackHandler"
    }

    fun onProgress(progress: Int) {
        Log.d(TAG, "[Thread: ${Thread.currentThread().name}] C++ thread is reporting progress: $progress%")
    }

    fun onComplete(result: String) {
        Log.d(TAG, "[Thread: ${Thread.currentThread().name}] C++ thread has completed with result: '$result'")
    }

    fun methodThatThrowsException() {
        Log.d(TAG, "This Java method is about to throw an exception!")
        throw RuntimeException("An intentional exception from Java")
    }
}