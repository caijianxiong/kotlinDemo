package com.kandaovr.meeting.kotlinDemo

import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.kandaovr.meeting.kotlinDemo.databinding.ActivityMainBinding
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private var TAG = this.javaClass.simpleName
    var var_c by Delegates.notNull<Int>()
    var miil by Delegates.notNull<Double>()

    private var myService: MyService? = null
    
    private lateinit var mDataBinding: ActivityMainBinding


    private var connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(TAG, "onServiceConnected: ")
            val myBinder = service as MyService.MyBinder
            myService = myBinder.service

            Log.i(TAG, "onServiceConnected service name:  ${myService!!.getServiceName()}")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i(TAG, "onServiceDisconnected: ")
        }

    }

    private var iBase=object :IBase{
        override fun ISetName() {

        }
    }

    lateinit var str: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding= ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(R.layout.activity_main)
        str = "hello"
        // Example of a call to a native method
        mDataBinding.sampleText.post(Runnable {
            mDataBinding.sampleText.requestFocus()
        })
        mDataBinding.sampleText.text = stringFromJNI()

        mDataBinding.btStartService.setOnClickListener { v->
            var intent = Intent(this, MyService::class.java)
            bindService(intent, connection, BIND_AUTO_CREATE)
        }

        mDataBinding.btStopService.setOnClickListener {
            unbindService(connection)
        }

    }

    fun getStr(name: String): String {
        var num: Int
        if (name.isEmpty()) {
            return ""
        }
        num = 123
        return "haha${num}"
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

//    companion object {
//        // Used to load the 'native-lib' library on application startup.
//        init {
//            System.loadLibrary("native-lib")
//        }
//    }

    interface Listener{
        fun onClick(v:View)
    }
}