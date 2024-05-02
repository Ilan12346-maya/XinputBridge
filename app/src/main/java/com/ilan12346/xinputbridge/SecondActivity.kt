package com.ilan12346.xinputbridge

import android.os.Bundle
import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    private lateinit var gamepadInputView: TextView

    private lateinit var gamepadNameTextView: TextView
    private lateinit var processInput: ProcessInput

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        gamepadInputView = findViewById(R.id.gamepadInputView)
        gamepadNameTextView = findViewById(R.id.gamepadNameTextView)
        processInput = ProcessInput()

        val backButton = findViewById<TextView>(R.id.refreshButton)
        backButton.text = "Back" // Ändere den Text des Buttons

        backButton.setOnClickListener {
            onBackPressed() // Aktion ändern, um den Zurück-Button auszuführen
        }

        val connectedGamepads = InputDevice.getDeviceIds().filter {
            val device = InputDevice.getDevice(it)
            device.sources and InputDevice.SOURCE_GAMEPAD == InputDevice.SOURCE_GAMEPAD
        }

        if (connectedGamepads.isEmpty()) {
            gamepadNameTextView.text = "No Gamepad"
        } else {
            val gamepadName = InputDevice.getDevice(connectedGamepads[0]).name
            gamepadNameTextView.text = gamepadName
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (!processInput.inputFromGamepad(event)) {
            return false
        }

        processInput.setButtonState(keyCode, false)
        gamepadInputView.text = processInput.InputStates
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (!processInput.inputFromGamepad(event)) {
            return false
        }

        processInput.setButtonState(keyCode, true)
        gamepadInputView.text = processInput.InputStates
        return true
    }

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        if (!processInput.inputFromGamepad(event)) {
            return false
        }

        processInput.processMotionEvent(event)
        gamepadInputView.text = processInput.InputStates
        return super.onGenericMotionEvent(event)
    }
}
