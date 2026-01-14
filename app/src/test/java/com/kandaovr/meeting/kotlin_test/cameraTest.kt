package com.kandaovr.meeting.kotlin_test

import android.graphics.SurfaceTexture
import android.view.TextureView
import com.kandaovr.meeting.kotlinDemo.camera.CameraFactory
import com.kandaovr.meeting.kotlinDemo.camera.Camera

fun main() {

    /**
     *
     * mainActivity
     * int mode
     * CameraProxy mCamera
     *
     * // open camera
     * mCamera=CameraFactory.getCamera(mode);
     * mCamera.openCamera()
     *
     * // release camera
     * mCamera.releaseCamera()
     *
     */

    val factory = CameraFactory()

    var cameraType = Camera.CameraType.CAMERA_LOCAL

    val camera = factory.getCamera(cameraType)
//    camera.openCamera();


}


private val mSurfaceTextureListener = object : TextureView.SurfaceTextureListener {
    override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
        TODO("Not yet implemented")
    }

    override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {
        TODO("Not yet implemented")
    }


}