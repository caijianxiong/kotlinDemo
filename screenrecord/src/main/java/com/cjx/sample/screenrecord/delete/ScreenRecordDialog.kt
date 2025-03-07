//package com.cjx.sample.screenrecord.delete
//
//import android.app.Activity
//import android.app.AlertDialog
//import android.app.PendingIntent
//import android.content.Context
//
//class ScreenRecordDialog(private val context: Context?,
//                         private val recordingController: RecordingController,
//                         private val onStartRecordingClicked: Runnable) : AlertDialog(context) {
//    private val DELAY_MS: Long = 3000
//    private val INTERVAL_MS: Long = 1000
//
//
//    private fun requestScreenCapture(userContext: Context) {
//        val audioMode: ScreenRecordingAudioSource = ScreenRecordingAudioSource.MIC_AND_INTERNAL
//        val startIntent = PendingIntent.getForegroundService(userContext,
//                                                             RecordingService.REQUEST_CODE,
//                                                             RecordingService.getStartIntent(
//                                                                 userContext,
//                                                                 Activity.RESULT_OK,
//                                                                 audioMode.ordinal),
//                                                             PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
//        val stopIntent = PendingIntent.getService(userContext,
//                                                  RecordingService.REQUEST_CODE,
//                                                  RecordingService.getStopIntent(
//                                                      userContext),
//                                                  PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
//        recordingController.startCountdown(DELAY_MS, INTERVAL_MS, startIntent, stopIntent)
//    }
//}