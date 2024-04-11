package com.kandaovr.meeting.kotlinDemo.camera

interface Camera {

    enum class CameraType {
        CAMERA_LOCAL, CAMERA_HDMI_IN
    }

    enum class CameraState {
        CAMERA_STATE_CLOSED, CAMERA_STATE_OPENED, CAMERA_STATE_CAPTURING, CAMERA_STATE_BUSY,
    }

    var cameraId: String?

//    fun openCamera()
//    fun startPreView()
//    fun stopPreview()
//    fun releaseCamera()
}