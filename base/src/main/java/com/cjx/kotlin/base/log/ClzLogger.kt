package com.cjx.kotlin.base.log

import android.text.TextUtils
import com.cjx.kotlin.base.log.ClzLogger.LoggerHelper.wrapMsg
import com.cjx.kotlin.base.log.ClzLogger.LoggerHelper.wrapMsgWithCheck
import com.orhanobut.logger.Logger

object ClzLogger {
    @JvmStatic
    fun v(instance: Any, msg: String, vararg args: Any) {
        wrapMsg(instance, msg, false).also {
            Logger.v(it, *args)
        }
    }

    @JvmStatic
    fun vm(instance: Any, msg: String, vararg args: Any) {
        wrapMsg(instance, msg, true).also {
            Logger.v(it, *args)
        }
    }

    @JvmStatic
    fun vv(instance: Any, msg: String, vararg args: Any) {
        wrapMsgWithCheck(instance, msg, true)?.let {
            Logger.v(it, *args)
        }
    }

    @JvmStatic
    fun d(instance: Any, msg: String, vararg args: Any) {
        wrapMsg(instance, msg, true).also {
            Logger.d(it, *args)
        }
    }

    @JvmStatic
    fun dnm(instance: Any, msg: String, vararg args: Any) {
        wrapMsg(instance, msg, false).also {
            Logger.d(it, *args)
        }
    }

    @JvmStatic
    fun dd(instance: Any, msg: String, vararg args: Any) {
        wrapMsgWithCheck(instance, msg, true)?.let {
            Logger.d(it, *args)
        }
    }

    @JvmStatic
    fun i(instance: Any, msg: String, vararg args: Any) {
        wrapMsg(instance, msg, false).also {
            Logger.i(it, *args)
        }
    }

    @JvmStatic
    fun ii(instance: Any, msg: String, vararg args: Any) {
        wrapMsgWithCheck(instance, msg, true)?.let {
            Logger.i(it, *args)
        }
    }

    @JvmStatic
    fun w(instance: Any, msg: String, vararg args: Any) {
        wrapMsg(instance, msg, false).also {
            Logger.w(it, *args)
        }
    }

    @JvmStatic
    fun ww(instance: Any, msg: String, vararg args: Any) {
        wrapMsgWithCheck(instance, msg, true)?.let {
            Logger.w(it, *args)
        }
    }

    @JvmStatic
    fun e(instance: Any, msg: String, vararg args: Any) {
        wrapMsg(instance, msg, false).also {
            Logger.e(it, *args)
        }
    }

    @JvmStatic
    fun ee(instance: Any, msg: String, vararg args: Any) {
        wrapMsgWithCheck(instance, msg, true)?.let {
            Logger.e(it, *args)
        }
    }

    @JvmStatic
    fun e(instance: Any, e: Throwable, msg: String, vararg args: Any) {
        wrapMsg(instance, msg, false).also {
            Logger.e(e, it, *args)
        }
    }

    @JvmStatic
    fun ee(instance: Any, e: Throwable, msg: String, vararg args: Any) {
        wrapMsgWithCheck(instance, msg, true)?.let {
            Logger.e(e, it, *args)
        }
    }

    private object LoggerHelper {
        private const val PROP_PREFIX = "sys.debug.clzlogger"

        init {
//            if (TextUtils.isEmpty(SystemProperties.get("$PROP_PREFIX.ALL"))) {
//                SystemProperties.set("$PROP_PREFIX.ALL", "false")
//            }
        }

        private fun checkLoggableByClassName(className: String?): Boolean {
//            val all = SystemProperties.getBoolean("$PROP_PREFIX.ALL", false)
//            return if (className == null) {
//                all
//            } else {
//                all
//                        || SystemProperties.getBoolean("$PROP_PREFIX.$className", false)
//                        || zzSystemProperties
//                    .getBoolean("$PROP_PREFIX.${className.uppercase()}", false)
//                        || SystemProperties
//                    .getBoolean("$PROP_PREFIX.${className.lowercase()}", false)
//            }
            return false
        }

        fun wrapMsg(instance: Any, msg: String, detailedTag: Boolean = false): String {
            return wrapMsgIfPossible(instance, msg, detailedTag, false)!!
        }

        fun wrapMsgWithCheck(instance: Any, msg: String, detailedTag: Boolean = false): String? {
            return wrapMsgIfPossible(instance, msg, detailedTag, true)
        }

        private fun wrapMsgIfPossible(
            instance: Any, msg: String, detailedTag: Boolean = false, checkLoggable: Boolean = false
        ): String? {
            val clzName = when (instance) {
                is String -> {
                    instance
                }

                is Class<*> -> {
                    instance.simpleName
                }

                else -> {
                    instance::class.simpleName
                }
            }
            if (checkLoggable && checkLoggableByClassName(clzName).not()) {
                return null
            }

            val stackTrace = Thread.currentThread().stackTrace

            var details = ""
            if (detailedTag) {
//                val index = stackTrace.indexOfFirst {
//                    it.className.contains("ClzLogger")
//                            && it.className.contains("LoggerHelper").not()
//                }
//                val caller = stackTrace[index + 1]
                // 加速
                val caller = stackTrace[5]
                val fileName = caller?.fileName ?: "UnknownFile"
                val lineNumber = caller?.lineNumber ?: -1
                val methodName = caller?.methodName ?: "UnknownMethod"

                val fileDetails = "★(${fileName}:${lineNumber})"
                val methodDetails = "[$methodName]"
                details = if (msg.contains(methodDetails)) {
                    fileDetails
                } else {
                    fileDetails + methodDetails
                }
            }
            return "<$clzName> $msg $details"
        }
    }
}