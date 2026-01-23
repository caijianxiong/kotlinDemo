package com.kandaovr.meeting.kotlinDemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.kandaovr.meeting.kotlinDemo.databinding.ActivityMainBinding
import com.kandaovr.meeting.kotlinDemo.jni.JniCallbackHandler
import com.kandaovr.meeting.kotlinDemo.jni.NativeLib
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var TAG = this.javaClass.simpleName
    private lateinit var mDataBinding: ActivityMainBinding
    private lateinit var jniCallbackHandler: JniCallbackHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(mDataBinding.root)

        // 初始化回调处理器
        jniCallbackHandler = JniCallbackHandler()

        // --- JNI Advanced Test Button ---
        mDataBinding.btJniCallback.setOnClickListener {
            Log.d(TAG, "Button clicked, starting native thread...")
            NativeLib.startNativeThread(jniCallbackHandler)
            mDataBinding.sampleText.text = "Native thread started. Check Logcat for progress."
        }

        // --- MVI Test Button ---
        // 使用隐式 Intent 启动 MVI 测试页面，实现模块解耦
        mDataBinding.btMviTest.setOnClickListener { // Assumes you have a button with this ID
            Log.d(TAG, "Button clicked, starting MVI test activity...")
            val intent = Intent("com.kandaovr.mvi.test.OPEN")
            // 添加此检查可以防止在找不到 Activity 时应用崩溃
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Log.e(TAG, "MVI Test Activity not found!")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called, cleaning up JNI resources.")
        // 在 Activity 销毁时，通知 C++ 层清理全局引用
        NativeLib.cleanup()
    }
}
