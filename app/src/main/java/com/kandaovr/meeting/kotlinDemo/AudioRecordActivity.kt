package com.kandaovr.meeting.kotlinDemo

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaCodec
import android.media.MediaCodecList
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.IOException


class AudioRecordActivity: AppCompatActivity() {


    private val REQUEST_PERMISSION_CODE = 1
    private val TAG = "RecorderActivity"

    private var mediaRecorder: MediaRecorder? = null
    private var fileName: String? = null
    private var isRecording = false
    private var recordButton: Button? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recorder)
        recordButton = findViewById<Button>(R.id.recordButton)

        // 检查权限
        if (ContextCompat.checkSelfPermission(this,
                                              Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                                              arrayOf<String>(Manifest.permission.RECORD_AUDIO,
                                                              Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                              Manifest.permission.READ_EXTERNAL_STORAGE
                                              ),
                                              REQUEST_PERMISSION_CODE)
        }
        recordButton?.setOnClickListener(View.OnClickListener {
            if (isRecording) {
                stopRecording()
            } else {
                startRecording()
            }
        })
    }

    private fun startRecording() {
        // 设置文件路径
        val filePath = getFilePath()
        val outputFile = File(filePath)

        // 创建MediaRecorder实例
        mediaRecorder = MediaRecorder()
        try {
            // 设置录音参数
            mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mediaRecorder!!.setAudioChannels(2) // 双声道
            mediaRecorder!!.setAudioSamplingRate(16000) // 采样率16000
            mediaRecorder!!.setAudioEncodingBitRate(128000) // 码率128000
            mediaRecorder!!.setOutputFile(outputFile.absolutePath)

            // 准备并开始录音
            mediaRecorder!!.prepare()
            mediaRecorder!!.start()
            isRecording = true
            recordButton!!.text = "停止录音"
        } catch (e: IOException) {
            Log.e(TAG, "录音失败: ", e)
            Toast.makeText(this, "录音失败", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder!!.stop()
                mediaRecorder!!.release()
                mediaRecorder = null
                isRecording = false
                recordButton!!.text = "开始录音"
                Toast.makeText(this, "录音已保存: $fileName", Toast.LENGTH_SHORT).show()
            } catch (e: RuntimeException) {
                // 处理异常
                Log.e(TAG, "停止录音失败: ", e)
            }
        }
    }

    // 获取录音文件路径
    private fun getFilePath(): String {
        // 获取应用目录
        val dir = getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        if (dir != null && !dir.exists()) {
            dir.mkdirs() // 如果目录不存在则创建
        }
        // 根据文件名增加后缀
        var fileIndex = 1
        val baseFileName = "recording_"
        fileName = "$baseFileName$fileIndex.m4a"
        var file = File(dir, fileName)

        // 如果文件已存在，文件名后缀递增
        while (file.exists()) {
            fileIndex++
            fileName = "$baseFileName$fileIndex.m4a"
            file = File(dir, fileName)
        }
        return file.absolutePath
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限获取成功，继续执行操作
            } else {
                Toast.makeText(this, "权限被拒绝，无法录音", Toast.LENGTH_SHORT).show()
            }
        }
    }

}