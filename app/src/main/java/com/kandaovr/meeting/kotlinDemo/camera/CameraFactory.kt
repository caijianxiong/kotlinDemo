package com.kandaovr.meeting.kotlinDemo.camera

class CameraFactory {

    fun getCamera(cameraType: Camera.CameraType): Camera {
        var cameraId = if (cameraType == Camera.CameraType.CAMERA_LOCAL) "0" else "140"
        return when (cameraType) {
            Camera.CameraType.CAMERA_LOCAL -> CameraProxy(cameraId)
            Camera.CameraType.CAMERA_HDMI_IN -> CameraHdmiProxy(cameraId)
        }
    }
}