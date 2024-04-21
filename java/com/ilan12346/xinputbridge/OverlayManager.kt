package com.ilan12346.xinputbridge


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast


@SuppressLint("StaticFieldLeak")
object OverlayManager {


    private lateinit var processInput: ProcessInput
    val wine_gamepad by lazy {NetworkManager()}
    private var wine_gamepadName = "No Gamepad"


    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private var overlayView: View? = null
    private var isActive = false


    @SuppressLint("ClickableViewAccessibility")
    fun showOverlay(context: Context, initialX: Int, initialY: Int) {
        wine_gamepad.startServer()
        processInput = ProcessInput()

        val connectedGamepads =  InputDevice.getDeviceIds().filter {
            val device = InputDevice.getDevice(it)
            device.sources and InputDevice.SOURCE_GAMEPAD == InputDevice.SOURCE_GAMEPAD
        }

        wine_gamepadName = if (connectedGamepads.isEmpty()) {
            "No Gamepad"
        } else {
            InputDevice.getDevice(connectedGamepads[0]).name
        }

        wine_gamepad.setGamepadName(wine_gamepadName)

        overlayView = View(context).apply {
            setBackgroundColor(Color.argb(150,30,0,0))
            val params = WindowManager.LayoutParams(
                150,
                150,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.TOP or Gravity.START
            }
            layoutParams = params
            setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val x = event.rawX.toInt()
                    val y = event.rawY.toInt()
                    if (x >= initialX && x <= initialX + width+70 && y >= initialY && y <= initialY + height+70) {
                        toggleActivation(context)
                    }
                    true
                } else {
                    false
                }
            }
            setOnKeyListener { _, keyCode, event ->
                if (isActive) {
                    if (event.action == KeyEvent.ACTION_DOWN) {
                        processInput.setButtonState(keyCode, true)
                        sendGamePad()





                       // Toast.makeText(context, processInput.InputStates, Toast.LENGTH_SHORT).show()
                        true
                    } else if (event.action == KeyEvent.ACTION_UP) {
                        processInput.setButtonState(keyCode, false)
                        sendGamePad()




                        true
                    } else {
                        false
                    }
                } else {
                    false
                }
            }

            setOnGenericMotionListener { _, motionEvent ->
                if (isActive) {
                    processInput.processMotionEvent(motionEvent)
                    sendGamePad()
                    true
                } else {
                    false
                }
            }

        }

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.addView(overlayView, overlayView?.layoutParams)
    }

    private fun toggleActivation(context: Context) {
        overlayView?.let { view ->
            val params = view.layoutParams as WindowManager.LayoutParams

            if (isActive) {
                view.setBackgroundColor(Color.argb(150,30,0,0))
                params.flags = params.flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

              //  Toast.makeText(context, "Overlay Deaktiviert", Toast.LENGTH_SHORT).show()
            } else {
                view.setBackgroundColor(Color.argb(20,30,0,0))
                params.flags = params.flags and WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE.inv()

              //  Toast.makeText(context, "Overlay aktiviert", Toast.LENGTH_SHORT).show()
            }

            val windowManager = view.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.updateViewLayout(view, params)
            isActive = !isActive
        }
    }

    fun sendGamePad(){

        wine_gamepad.BUTTON_A = processInput.BUTTON_A
        wine_gamepad.BUTTON_B = processInput.BUTTON_B
        wine_gamepad.BUTTON_X = processInput.BUTTON_X
        wine_gamepad.BUTTON_Y = processInput.BUTTON_Y
        wine_gamepad.BUTTON_R1 = processInput.BUTTON_R1
        wine_gamepad.BUTTON_R2 = processInput.BUTTON_R2
        wine_gamepad.BUTTON_R3 = processInput.BUTTON_R3
        wine_gamepad.BUTTON_L1 = processInput.BUTTON_L1
        wine_gamepad.BUTTON_L2 = processInput.BUTTON_L2
        wine_gamepad.BUTTON_L3 = processInput.BUTTON_L3
        wine_gamepad.BUTTON_START = processInput.BUTTON_START
        wine_gamepad.BUTTON_SELECT = processInput.BUTTON_SELECT
        wine_gamepad.DPAD_DIRECTION = processInput.DPAD.toInt()
        wine_gamepad.LEFT_X = processInput.STICK_LX.toInt()
        wine_gamepad.LEFT_Y = processInput.STICK_LY.toInt()
        wine_gamepad.RIGHT_X = processInput.STICK_RX.toInt()
        wine_gamepad.RIGHT_Y = processInput.STICK_RY.toInt()
        }

    fun removeOverlay(context: Context) {
        wine_gamepad.stopServer()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        overlayView?.let {
            windowManager.removeView(it)
            overlayView = null
        }
    }
}
