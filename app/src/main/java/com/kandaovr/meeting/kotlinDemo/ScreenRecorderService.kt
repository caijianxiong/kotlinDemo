package com.kandaovr.meeting.kotlinDemo

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.os.Binder
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.kandaovr.meeting.kotlinDemo.audio.ScreenInternalAudioRecorder
import com.kandaovr.meeting.kotlinDemo.audio.ScreenRecordingAudioSource
import java.io.File
import java.io.IOException


class ScreenRecorderService : Service() {

    private val TAG = this.javaClass.simpleName
    private var mAudio: ScreenInternalAudioRecorder? = null
    var mediaProjection: MediaProjection? = null
    private var mediaRecorder: MediaRecorder? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var screenWidth = 0
    private var screenHeight: Int = 0
    private var screenDensity: Int = 0

    private var mTempAudioFile: File? = null

    public var isBound = false

    private var mAudioSource = ScreenRecordingAudioSource.MIC_AND_INTERNAL


    // 自定义 Binder 用于传递 MediaProjection
    inner class LocalBinder : Binder() {
//        fun setMediaProjection(projection: MediaProjection) {
//            this@ScreenRecorderService.mediaProjection = projection
//            Log.d(TAG, "setMediaProjection: "+this@ScreenRecorderService.mediaProjection)
//            startRecording() // 开始录制
//        }

        fun getService(): ScreenRecorderService? {
            return this@ScreenRecorderService
        }
    }

    fun setsssMediaProjection(projection: MediaProjection) {
        Log.d(TAG, "setMediaProjection: " + projection)
        mediaProjection = projection
        startRecording() // 开始录制
    }

    private val binder: IBinder = LocalBinder()


    override fun onCreate() {
        super.onCreate()
//        // 创建前台通知
//        // 创建前台通知
//        val notification: Notification =
//            Notification.Builder(this, "screen_record_channel").setContentTitle("屏幕录制中")
//                .setSmallIcon(R.drawable.ic_launcher_background).build()
//        startForeground(1, notification)


        val channel = NotificationChannel("CHANNEL_ID",
                                          "Recording Channel",
                                          NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val notification =
            NotificationCompat.Builder(this, "CHANNEL_ID").setContentTitle("Recording")
                .setContentText("Recording is in progress")
                .setSmallIcon(R.drawable.ic_launcher_foreground).build()
//         启动前台服务
        startForeground(1, notification)

        // 获取屏幕参数
        val metrics = resources.displayMetrics
        screenWidth = metrics.widthPixels
        screenHeight = metrics.heightPixels
        screenDensity = metrics.densityDpi
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind: ")
//        stopRecording()
        return super.onUnbind(intent)
    }

    fun startRecording() {
        if (mediaProjection == null) {
            Log.w(TAG, "startRecording return: ")
            return
        }
        isBound = true;
        try {
            initMediaRecorder()
            createVirtualDisplay()
            mediaRecorder!!.start()
            if (mAudioSource === ScreenRecordingAudioSource.INTERNAL || mAudioSource === ScreenRecordingAudioSource.MIC_AND_INTERNAL) {
                mAudio!!.start()
            }
            showToast("开始录屏")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("WrongConstant")
    @Throws(IOException::class)
    private fun initMediaRecorder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mediaRecorder = MediaRecorder(applicationContext)
        } else {
            Log.e(TAG, "initMediaRecorder mediaRecorder is null: ")
        }

        // 配置系统音频（Android 10+）
        if (mAudioSource == ScreenRecordingAudioSource.MIC) {
            mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC) // REMOTE_SUBMIX
            mediaRecorder!!.setAudioChannels(2)
            mediaRecorder!!.setAudioSamplingRate(44100)
            mediaRecorder!!.setAudioEncodingBitRate(128000)
        }


        // 配置视频参数
        mediaRecorder!!.setVideoSource(MediaRecorder.VideoSource.SURFACE)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        if (mAudioSource == ScreenRecordingAudioSource.MIC) {
            // set after setOutputFormat
            mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        }
        mediaRecorder!!.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
        mediaRecorder!!.setVideoSize(screenWidth, screenHeight)
        mediaRecorder!!.setVideoEncodingBitRate(5 * 1024 * 1024) // 5 Mbps
        mediaRecorder!!.setVideoFrameRate(30)


        // 设置输出文件（适配 Android 12 存储）
        mediaRecorder!!.setOutputFile(getOutputFile(this))
        mediaRecorder!!.prepare()


        if (mAudioSource === ScreenRecordingAudioSource.INTERNAL || mAudioSource === ScreenRecordingAudioSource.MIC_AND_INTERNAL) {
            mTempAudioFile = getAudioOutputFile()
            Log.d(TAG, "initMediaRecorder mTempAudioFile:${mTempAudioFile?.path} ")
            mAudio = ScreenInternalAudioRecorder(mTempAudioFile!!.getAbsolutePath(),
                                                 mediaProjection!!,
                                                 mAudioSource == ScreenRecordingAudioSource.MIC_AND_INTERNAL)
        }

    }

    private fun createVirtualDisplay() {
        virtualDisplay = mediaProjection!!.createVirtualDisplay("ScreenRecorder",
                                                                screenWidth,
                                                                screenHeight,
                                                                screenDensity,
                                                                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                                                                mediaRecorder!!.surface,
                                                                null,
                                                                null)
    }

    private fun getAudioOutputFile():File?{
        val dir =
            File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "screenrecord")
        if (!dir.exists()) dir.mkdirs()
        return File.createTempFile("temp", ".aac", dir)
    }
    private fun getOutputFile(context: Context): String? {
        // 传统文件路径
        val dir =
            File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "screenrecord")
        if (!dir.exists()) dir.mkdirs()
        dir.listFiles().let {
            for (file in it!!) {
                file.delete()
            }
        }
        val path = File(dir, "record_" + System.currentTimeMillis() + ".mp4").absolutePath
        Log.d(TAG, "getOutputFile: $path")
        return path
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecording()
    }

    fun stopRecording() {
        try {
            showToast("停止录屏")
            if (mediaRecorder != null) {
                mediaRecorder!!.stop()
                mediaRecorder!!.reset()
                mediaRecorder = null
            }
            if (virtualDisplay != null) {
                virtualDisplay!!.release()
                virtualDisplay = null
            }
            if (mediaProjection != null) {
                mediaProjection!!.stop()
                mediaProjection = null
            }

            if (mAudioSource === ScreenRecordingAudioSource.INTERNAL || mAudioSource === ScreenRecordingAudioSource.MIC_AND_INTERNAL) {
                mAudio?.end()
                mAudio = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isBound = false
        }
    }


}