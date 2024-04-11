package com.kandaovr.meeting.kotlinDemo.camera

class CameraProxy(override var cameraId: String?) :Camera {
    fun openCamera() {
        TODO("Not yet implemented")
    }

    fun startPreView() {
        TODO("Not yet implemented")
    }

    fun stopPreview() {
        TODO("Not yet implemented")
    }

    fun releaseCamera() {
        TODO("Not yet implemented")
    }


    // custom fun


    interface OnCameraStateCallBack{
        fun onOpen()
    }

}