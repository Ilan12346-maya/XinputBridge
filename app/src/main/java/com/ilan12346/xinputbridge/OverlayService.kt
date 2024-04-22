package com.ilan12346.xinputbridge

import android.os.IBinder
import android.view.View
import android.app.Service
import android.content.Intent


class OverlayService : Service() {

    private var overlayView: View? = null
    private var initialX = 0
    private var initialY = 0
    private var run = false

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!run) {
            run = true
            showOverlay()
            println("I! Start Overlay")
        } else {
            println("E! Overlay running")
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {

        if (run) {
            run = false
            super.onDestroy()
            removeOverlay()
            println("I! Overlay Stopped")
        } else {
            println("E! Overlay not running")
        }


    }

    private fun showOverlay() {
        OverlayManager.showOverlay(this, initialX, initialY)
    }


    private fun removeOverlay() {
        OverlayManager.removeOverlay(this)
    }


}
