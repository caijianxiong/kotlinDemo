package com.kandaovr.meeting.kotlinDemo

import android.app.Application
import com.kandaovr.meeting.rksdk.MeetingApi

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MeetingApi.getInstance().init(this)
    }
}