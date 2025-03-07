package com.kandaovr.meeting.kotlinDemo.ui

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kandaovr.meeting.kotlinDemo.R
import com.kandaovr.meeting.kotlinDemo.ScreenRecorderService
import com.kandaovr.meeting.kotlinDemo.ScreenRecorderService.LocalBinder
import com.kandaovr.meeting.kotlinDemo.databinding.ActivityScreenRecordBinding


class ScreenRecordActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName
    private val REQUEST_CODE_SCREEN_CAPTURE = 1
    private var mediaProjectionManager: MediaProjectionManager? = null
    private var mediaProjection: MediaProjection? = null

    lateinit var mBinding: ActivityScreenRecordBinding


    private var screenRecorderService: ScreenRecorderService? = null

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LocalBinder
//            binder.setMediaProjection(mediaProjection!!)
            screenRecorderService = binder.getService()
            // 启动权限请求
            startActivityForResult(mediaProjectionManager!!.createScreenCaptureIntent(),
                                   REQUEST_CODE_SCREEN_CAPTURE)
            Log.d(TAG, "onServiceConnected: ")
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.d(TAG, "onServiceDisconnected: ")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityScreenRecordBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mediaProjectionManager =
            getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        mBinding.recordStart.setOnClickListener {
            Log.d(TAG, "onCreate: isBound ${screenRecorderService?.isBound}")
            if (screenRecorderService?.isBound == true) {
                return@setOnClickListener
            }
            // 绑定 Service
            Log.d(TAG, "onCreate: bindService")
            val serviceIntent = Intent(this, ScreenRecorderService::class.java)
            bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE)
        }
        mBinding.recordEnd.setOnClickListener {
            Log.d(TAG, "onCreate: isBound ${screenRecorderService?.isBound}")
            if (screenRecorderService?.isBound == true) {
                Log.d(TAG, "onCreate: unbindService")
                unbindService(serviceConnection)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SCREEN_CAPTURE) {
            if (resultCode == RESULT_OK) {
                mediaProjection = mediaProjectionManager!!.getMediaProjection(resultCode, data!!)
                // 3. 通过 Binder 传递 MediaProjection
                mediaProjectionManager?.getMediaProjection(resultCode, data!!)?.let { projection ->
                    screenRecorderService?.setsssMediaProjection(projection)
                }
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }

}