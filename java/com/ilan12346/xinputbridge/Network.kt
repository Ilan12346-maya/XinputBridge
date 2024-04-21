package com.ilan12346.xinputbridge

import android.content.Context
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.nio.ByteBuffer
import java.nio.ByteOrder

//class NetworkManager(private val context: Context) {
class NetworkManager() {
    private lateinit var socket: DatagramSocket


    private val gamepadNameBYTE = byteArrayOf(
        0x08.toByte(), 0x03.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x01.toByte(),
        0x1F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x49.toByte(), 0x4C.toByte(),
        0x41.toByte(), 0x4E.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
        0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
        0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
        0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
        0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
        0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
        0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
        0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
        0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte()
    )

    val gamepadStateBYTE = byteArrayOf(
        0x09.toByte(), 0x01.toByte(), 0x03.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
        0x00.toByte(), 0x00.toByte(), 0xFF.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
        0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x00.toByte(),
        0x00.toByte(), 0x00.toByte(), 0x00.toByte()
    )


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
        var DPAD_DIRECTION = 255


        var LEFT_X = 0
        var LEFT_Y = 0
        var RIGHT_X = 0
        var RIGHT_Y = 0




    fun startServer() {
        Thread {
            val port = 7947

            try {
                socket = DatagramSocket(port)
                val buffer = ByteArray(64)

                while (true) {
                    val packet = DatagramPacket(buffer, buffer.size)
                    socket.receive(packet)

                    val firstByte = packet.data.firstOrNull()
                    val response = generateResponse(firstByte)

                    val responsePacket =
                        DatagramPacket(response, response.size, packet.address, packet.port)
                    socket.send(responsePacket)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun generateResponse(firstByte: Byte?): ByteArray {
        return when (firstByte?.toInt()) {
            9 -> {
                UpdateGamepadstate()
                gamepadStateBYTE
            }
            8 -> gamepadNameBYTE
            else -> byteArrayOf()
        }
    }

    fun stopServer() {
        if (::socket.isInitialized && !socket.isClosed) {
            socket.close()
        }
    }

    fun setGamepadName(name: String) {
        val nameBytes = name.toByteArray()

        gamepadNameBYTE[7] = nameBytes.size.toByte()
        for (i in 0 until nameBytes.size) {
            gamepadNameBYTE[10 + i] = nameBytes[i]
        }
    }

    private fun UpdateGamepadstate() {


        gamepadStateBYTE[6] = (((if (BUTTON_START) 1 else 0) shl 7) or
                ((if (BUTTON_SELECT) 1 else 0) shl 6) or
                ((if (BUTTON_R1) 1 else 0) shl 5) or
                ((if (BUTTON_L1) 1 else 0) shl 4) or
                ((if (BUTTON_Y) 1 else 0) shl 3) or
                ((if (BUTTON_X) 1 else 0) shl 2) or
                ((if (BUTTON_B) 1 else 0) shl 1) or
                (if (BUTTON_A) 1 else 0)).toByte()

        gamepadStateBYTE[7] = (((if (BUTTON_R2) 1 else 0) shl 3) or
                ((if (BUTTON_L2) 1 else 0) shl 2) or
                ((if (BUTTON_R3) 1 else 0) shl 1) or
                (if (BUTTON_L3) 1 else 0)).toByte()



            gamepadStateBYTE[8] = DPAD_DIRECTION.toByte()



        val byte_LX = ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putShort(LEFT_X.toShort()).array()
        val byte_LY = ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putShort(LEFT_Y.toShort()).array()
        val byte_RX = ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putShort(RIGHT_X.toShort()).array()
        val byte_RY = ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putShort(RIGHT_Y.toShort()).array()

        gamepadStateBYTE[9] = byte_LX[1]
        gamepadStateBYTE[10] = byte_LX[0]
        gamepadStateBYTE[11] = byte_LY[1]
        gamepadStateBYTE[12] = byte_LY[0]
        gamepadStateBYTE[13] = byte_RX[1]
        gamepadStateBYTE[14] = byte_RX[0]
        gamepadStateBYTE[15] = byte_RY[1]
        gamepadStateBYTE[16] = byte_RY[0]


    }


}




