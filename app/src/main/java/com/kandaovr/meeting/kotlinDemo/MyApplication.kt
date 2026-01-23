package com.kandaovr.meeting.kotlinDemo

import android.app.Application
import android.util.Log
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    private var TAG = "MyApplication"

    override fun onCreate() {
        super.onCreate()
        Logger.addLogAdapter(AndroidLogAdapter())
        Log.d(TAG, "onCreate: ")
    }
}