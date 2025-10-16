package com.cjx.kotlin.base.log

import android.content.Context
import android.os.Build
import android.os.HandlerThread
import android.os.SystemProperties
import com.cjx.kotlin.base.BuildConfig
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.CsvFormatStrategy
import com.orhanobut.logger.DiskLogAdapter
import com.orhanobut.logger.DiskLogStrategy
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger

class LoggerManager private constructor() {

    companion object {
        private const val TAG = BuildConfig.LOG_TAG
        private const val TAG_WITH_DETAILS = TAG + "_DETAILS"

        private const val PROP_LOGGER_ENABLED = "sys.meeting.logger.enabled"
        private const val PROP_METHOD_COUNT = "sys.meeting.logger.method"

        private var DEBUGGABLE = BuildConfig.DEBUG
                || Build.TYPE.lowercase() == "userdebug"
                || Build.TYPE.lowercase() == "eng"
                || SystemProperties.getBoolean(PROP_LOGGER_ENABLED, false)

        private val sInstance by lazy {
            LoggerManager()
        }

        fun getInstance(): LoggerManager {
            return sInstance
        }
    }

    private lateinit var context: Context
    private val logThread = HandlerThread("MEETING_LOGGER_THREAD")
    private var isInitialized = false

    fun initLogger(context: Context) {
        if (isInitialized) {
            Logger.w("Logger has already initialized!")
        }
        isInitialized = true
        this.context = context
        var formatStrategy: FormatStrategy = MyPrettyFormatStrategy.newBuilder()
            .methodCount(SystemProperties.getInt(PROP_METHOD_COUNT, 6))
            .showThreadInfo(true)
            .tag(TAG_WITH_DETAILS)
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return if (DEBUGGABLE) {
                    priority > Logger.DEBUG
                } else {
                    false
                }
            }
        })

        formatStrategy = MyPrettyFormatStrategy.newBuilder()
            .methodCount(0)
            .showThreadInfo(false)
            .tag(TAG)
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return if (DEBUGGABLE) {
                    priority <= Logger.DEBUG
                } else {
                    priority >= Logger.DEBUG
                }
            }
        })

        logThread.start()
        this@LoggerManager.context.getExternalFilesDir("logger")?.let {
            val dir = it.absolutePath
            val logHandler = WriteLogHandler(logThread.looper, dir, 50 * 1024 * 1024)
            val csvFormatStrategy: FormatStrategy = CsvFormatStrategy.newBuilder()
                .logStrategy(DiskLogStrategy(logHandler))
                .tag(TAG)
                .build()
            Logger.addLogAdapter(object : DiskLogAdapter(csvFormatStrategy) {
                override fun isLoggable(priority: Int, tag: String?): Boolean {
                    return if (DEBUGGABLE) {
                        priority > Logger.VERBOSE
                    } else {
                        false
                    }
                }
            })
        } ?: ClzLogger.w(this, "get dir files/logger failed.")
    }

    fun update() {
        DEBUGGABLE = BuildConfig.DEBUG
                || Build.TYPE.lowercase() == "userdebug"
                || Build.TYPE.lowercase() == "eng"
                || SystemProperties.getBoolean(PROP_LOGGER_ENABLED, false)
    }
}