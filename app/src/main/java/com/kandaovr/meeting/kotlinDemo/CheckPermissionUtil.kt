package com.kandaovr.meeting.kotlinDemo

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object CheckPermissionUtil {


    fun checkPermissions(context: Activity, arrayPermissions: Array<String>, requestCode: Int) {
        var unGrantPermissions = mutableListOf<String>()
        for (permission in arrayPermissions) {
            if (ContextCompat.checkSelfPermission(context,
                                                  permission) != PackageManager.PERMISSION_GRANTED) {
                unGrantPermissions.add(permission)
            }
        }
        if (unGrantPermissions.isNotEmpty()) {
            Log.d("TAG", "checkPermissions: ${unGrantPermissions.size}")
            ActivityCompat.requestPermissions(context,
                                              unGrantPermissions.toTypedArray(),
                                              requestCode)
        } else {
            Log.d("TAG", "checkPermissions: all grant $arrayPermissions")
        }
    }

}