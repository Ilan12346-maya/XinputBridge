package com.ilan12346.xinputbridge

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity


class PermissionManager(private val context: Context) {

    private val OVERLAY_PERMISSION_REQUEST_CODE = 100

    fun checkOverlayPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            true
        }
    }

    fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${context.packageName}"))
            (context as? AppCompatActivity)?.startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE)
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            // Berechtigung erteilt
        }
    }
}
