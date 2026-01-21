package com.kandaovr.meeting.kotlinDemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.cjx.feature.user.presentation.UserActivity
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

        // 按钮点击事件
        mDataBinding.btJniCallback.setOnClickListener {
            Log.d(TAG, "Button clicked, starting native thread...")
            NativeLib.startNativeThread(jniCallbackHandler)
            mDataBinding.sampleText.text = "Native thread started. Check Logcat for progress."
        }

        // --- 其他原有按钮的逻辑 (为清晰起见，暂时简化或移除) ---
        mDataBinding.sampleText.setOnClickListener {
             startActivity(UserActivity.createIntent(this, 1001L))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called, cleaning up JNI resources.")
        // 在 Activity 销毁时，通知 C++ 层清理全局引用
        NativeLib.cleanup()
    }
}
