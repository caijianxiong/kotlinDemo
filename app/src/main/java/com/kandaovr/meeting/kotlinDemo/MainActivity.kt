package com.kandaovr.meeting.kotlinDemo

import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private var TAG = this.javaClass.simpleName
    var var_c by Delegates.notNull<Int>();
    var miil by Delegates.notNull<Double>()

    private var myService: MyService? = null

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


    lateinit var str: String;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        str = "hello"
        // Example of a call to a native method
        val tv = findViewById<TextView>(R.id.sample_text)
        tv?.post(Runnable {
            tv.requestFocus()
        })
        tv.text = stringFromJNI()

        bt_startService.setOnClickListener {
            var intent = Intent(this, MyService::class.java)
            bindService(intent, connection, BIND_AUTO_CREATE)
        }

        bt_stopService.setOnClickListener {
            unbindService(connection)
        }

    }

    fun getStr(name: String): String {
        var num: Int;
        if (name.isEmpty()) {
            return ""
        }
        num = 123
        return "haha${num}";
    }

    override fun finish() {
        super.finish()
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}