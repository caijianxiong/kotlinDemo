package com.kandaovr.meeting.kotlinDemo.test

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
    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
        TODO("Not yet implemented")
    }


}