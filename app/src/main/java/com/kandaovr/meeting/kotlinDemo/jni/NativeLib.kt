package com.kandaovr.meeting.kotlinDemo.jni

import android.util.Log

/**
 * 一个单例对象，用于集中管理所有 JNI 调用。
 */
object NativeLib {

    private const val TAG = "NativeLib"

    /**
     * 启动一个 C++ 原生线程来执行耗时任务，并通过回调接口将进度和结果返回给 Java/Kotlin 层。
     * @param handler 一个实现了回调逻辑的对象实例。
     */
    external fun startNativeThread(handler: JniCallbackHandler)

    /**
     * 清理在 C++ 层创建的全局引用和其他资源。
     * 应该在相关 Activity/Fragment 的 onDestroy() 中调用。
     */
    external fun cleanup()

    /**
     * 这个方法将被 C++ 回调。
     * 必须是静态的 (在 object 中，所有方法默认都是静态的)，这样 C++ 才能通过 GetStaticMethodID 找到它。
     * @JvmStatic 注解确保了会生成一个真正的 Java 静态方法，使 JNI 调用更简单、更可靠。
     */
    @JvmStatic
    fun callbackFromCpp(message: String) {
        Log.d(TAG, "===============================================")
        Log.d(TAG, "callbackFromCpp called with message: \"$message\"")
        Log.d(TAG, "===============================================")
    }

    // 在该对象首次被访问时，自动加载 so 库
    init {
        System.loadLibrary("native-lib")
    }
}
