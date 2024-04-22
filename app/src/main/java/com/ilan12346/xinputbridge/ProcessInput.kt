package com.ilan12346.xinputbridge

import android.view.InputDevice
import android.view.MotionEvent
import kotlin.math.round

class ProcessInput {

    private var Analog_Trigger = false


    var BUTTON_START = false
    var BUTTON_SELECT = false
    var BUTTON_R1 = false
    var BUTTON_L1 = false
    var BUTTON_Y = false
    var BUTTON_X = false
    var BUTTON_B = false
    var BUTTON_A = false
    var BUTTON_R2 = false
    var BUTTON_L2 = false
    var BUTTON_R3 = false
    var BUTTON_L3 = false
    var STICK_LX: Short = 0
    var STICK_LY: Short = 0
    var STICK_RX: Short = 0
    var STICK_RY: Short = 0
    var DPAD: Short = 255


    fun processMotionEvent(event: MotionEvent) {
        if (event.isFromSource(InputDevice.SOURCE_JOYSTICK)) {
            val xFloat = event.getAxisValue(MotionEvent.AXIS_X)
            val yFloat = event.getAxisValue(MotionEvent.AXIS_Y)
            val zFloat = event.getAxisValue(MotionEvent.AXIS_Z)
            val rzFloat = event.getAxisValue(MotionEvent.AXIS_RZ)
            val dxFloat = event.getAxisValue(MotionEvent.AXIS_HAT_X)
            val dyFloat = event.getAxisValue(MotionEvent.AXIS_HAT_Y)
            val rtFloat = event.getAxisValue(MotionEvent.AXIS_RTRIGGER)
            val ltFloat = event.getAxisValue(MotionEvent.AXIS_LTRIGGER)

            // Umrechnung in den Bereich von -32,768 bis 32,767
            STICK_LX = ((xFloat * 327).toInt() * 100).toShort()
            STICK_LY = ((yFloat * 327).toInt() * 100).toShort()
            STICK_RX = ((zFloat * 327).toInt() * 100).toShort()
            STICK_RY = ((rzFloat * 327).toInt() * 100).toShort()
            var TRIGGER_R = ((rtFloat * 10).toInt()).toShort()
            var TRIGGER_L = ((ltFloat * 10).toInt()).toShort()

            if (TRIGGER_R > 3) {
                BUTTON_R2 = true
                Analog_Trigger = true
            }

            if (TRIGGER_R < 3) {
                if (Analog_Trigger) {
                    BUTTON_R2 = false
                }
            }

            if (TRIGGER_L > 3) {
                BUTTON_L2 = true
                Analog_Trigger = true
            }

            if (TRIGGER_L < 3) {
                if (Analog_Trigger) {
                    BUTTON_L2 = false
                }
            }


            val dx = round(dxFloat.toDouble()).toInt()
            val dy = round(dyFloat.toDouble()).toInt()

            DPAD = getDpadDirection(dx, dy)
        }
    }

    fun setButtonState(keyCode: Int, pressed: Boolean) {

        when (keyCode) {
            108 -> BUTTON_START = pressed
            109 -> BUTTON_SELECT = pressed
            103 -> BUTTON_R1 = pressed
            102 -> BUTTON_L1 = pressed
            100 -> BUTTON_Y = pressed
            99 -> BUTTON_X = pressed
            97 -> BUTTON_B = pressed
            96 -> BUTTON_A = pressed
            105 -> BUTTON_R2 = pressed
            104 -> BUTTON_L2 = pressed
            107 -> BUTTON_R3 = pressed
            106 -> BUTTON_L3 = pressed


        }


    }


    private fun getDpadDirection(dx: Int, dy: Int): Short {
        return when {

            dx == 0 && dy == -1 -> 0 // DPAD_N
            dx == 1 && dy == -1 -> 1 // DPAD_NE
            dx == 1 && dy == 0 -> 2  // DPAD_E
            dx == 1 && dy == 1 -> 3  // DPAD_SE
            dx == 0 && dy == 1 -> 4  // DPAD_S
            dx == -1 && dy == 1 -> 5 // DPAD_SW
            dx == -1 && dy == 0 -> 6 // DPAD_W
            dx == -1 && dy == -1 -> 7 // DPAD_NW
            else -> 255 // No direction
        }
    }

    val InputStates: String
        get() {
            val stringBuilder = StringBuilder()
            stringBuilder.append("BUTTON_START = ").append(BUTTON_START).append("\n")
            stringBuilder.append("BUTTON_SELECT = ").append(BUTTON_SELECT).append("\n")
            stringBuilder.append("BUTTON_R1 = ").append(BUTTON_R1).append("\n")
            stringBuilder.append("BUTTON_L1 = ").append(BUTTON_L1).append("\n")
            stringBuilder.append("BUTTON_Y = ").append(BUTTON_Y).append("\n")
            stringBuilder.append("BUTTON_X = ").append(BUTTON_X).append("\n")
            stringBuilder.append("BUTTON_B = ").append(BUTTON_B).append("\n")
            stringBuilder.append("BUTTON_A = ").append(BUTTON_A).append("\n")
            stringBuilder.append("BUTTON_R2 = ").append(BUTTON_R2).append("\n")
            stringBuilder.append("BUTTON_L2 = ").append(BUTTON_L2).append("\n")
            stringBuilder.append("BUTTON_R3 = ").append(BUTTON_R3).append("\n")
            stringBuilder.append("BUTTON_L3 = ").append(BUTTON_L3).append("\n")
            stringBuilder.append("Left Stick X: ").append(STICK_LX).append("\n")
            stringBuilder.append("Left Stick Y: ").append(STICK_LY).append("\n")
            stringBuilder.append("Right Stick X: ").append(STICK_RX).append("\n")
            stringBuilder.append("Right Stick Y: ").append(STICK_RY).append("\n")
            stringBuilder.append("D-Pad: ").append(DPAD).append("\n")
            return stringBuilder.toString()
        }


}
