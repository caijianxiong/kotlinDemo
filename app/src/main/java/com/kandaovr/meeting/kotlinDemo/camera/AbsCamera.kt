package com.kandaovr.meeting.kotlinDemo.camera

abstract class AbsCamera :Camera {
    private var mCameraState=Camera.CameraState.CAMERA_STATE_CLOSED

    constructor()
}