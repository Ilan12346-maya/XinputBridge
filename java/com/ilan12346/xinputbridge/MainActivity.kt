package com.ilan12346.xinputbridge

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.view.View

class MainActivity : AppCompatActivity() {

    private lateinit var permissionManager: PermissionManager
    val networkManager by lazy {NetworkManager(this)}

    override fun onCreate(savedInstanceState: Bundle?) {





        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        OverlayManager.setNetworkManager(networkManager)

        permissionManager = PermissionManager(this)

        val startServiceButton: Button = findViewById(R.id.startServiceButton)
        val stopServiceButton: Button = findViewById(R.id.stopServiceButton)
        val grantPermissionsButton: Button = findViewById(R.id.grantPermissionsButton)
        val startSecondActivityButton: Button = findViewById(R.id.startSecondActivityButton)
        
        startSecondActivityButton.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        startServiceButton.setOnClickListener { startOverlayService() }
        stopServiceButton.setOnClickListener { stopOverlayService() }
        
        grantPermissionsButton.setOnClickListener {
            if (!permissionManager.checkOverlayPermission()) {
                permissionManager.requestOverlayPermission()
            }
        }

        if (permissionManager.checkOverlayPermission()) {
            grantPermissionsButton.visibility = View.GONE
        } else {
            grantPermissionsButton.visibility = View.VISIBLE
        }
    }

    private fun startOverlayService() {
    
        networkManager.startServer()
        startService(Intent(this, OverlayService::class.java))
    }

    private fun stopOverlayService() {
        stopService(Intent(this, OverlayService::class.java))
        networkManager.stopServer()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        permissionManager.onActivityResult(requestCode, resultCode, data)
    }
    
    
}
