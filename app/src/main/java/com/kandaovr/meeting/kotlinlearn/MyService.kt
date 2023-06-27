package com.kandaovr.meeting.kotlinlearn

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.Parcel
import android.os.Parcelable
import android.util.Log

class MyService : Service() {

    private var TAG = this.javaClass.simpleName
    private var myBinder = MyBinder()

    protected var num = 10200

    override fun onCreate() {
        Log.i(TAG, "onCreate: ")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand: ")
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        Log.i(TAG, "onBind: ")
        return myBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {

        Log.i(TAG, "onUnbind: ")
        return super.onUnbind(intent)
    }


    override fun onDestroy() {
        Log.i(TAG, "onDestroy: ")
        super.onDestroy()
    }

    fun getServiceName(): String {
        return this.javaClass.simpleName
    }

    inner class MyBinder : Binder() {
        // binder 获取service实例
        val service: MyService
            get() = this@MyService

        fun getName(): String {
            return getServiceName()
        }

    }
}