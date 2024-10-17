package com.jiali.android.lib_plugin_demo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.net.InetAddress
import java.nio.ByteBuffer
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*val str = "鲁AD12345  "
        val array = str.toByteArray(charset("GBK"))
        Log.d("tag", bytesToHex(array))
        val shortArray = shortArrayOf(1001,0,2,0,1,1)
        Log.d("tag", bytesToHex(shortArray.toByteArray()))
        Log.d("tag", bytesToHex(shortToByteArray(1001.toShort())))
        Log.d("tag", bytesToHex(shortToByteArray(0.toShort())))
        Log.d("tag", bytesToHex(shortToByteArray(2.toShort())))
        Log.d("tag", bytesToHex(shortToByteArray(0.toShort())))
        Log.d("tag", bytesToHex(shortToByteArray(1.toShort())))
        Log.d("tag", bytesToHex(shortToByteArray(1.toShort())))*/
        Log.d("tag", changeBitValue(0, 5, 1).toString())

        thread {
           try {
               Log.d("tag", InetAddress.getByName("parking.fsjlkj.cn").hostAddress ?: "啥都沒有")
           }catch (e:Exception){
               Log.d("tag","報錯了：${e.message}")
           }
        }
    }

    private fun changeBitValue(num: Int, bitPosition: Int, newBitValue: Int): Int {
        // 清除第bitPosition位
        var result = num and (1 shl bitPosition).inv()
        // 设置第bitPosition位为newBitValue
        result = result or (newBitValue shl bitPosition)
        return result
    }


    fun ShortArray.toByteArray(): ByteArray {
        val byteArray = ByteArray(size * 2)
        for (i in indices) {
            val short = this[i]
            byteArray[i * 2] = (short.toInt() shr 8).toByte() // 高位
            byteArray[i * 2 + 1] = (short.toInt() and 0xFF).toByte() // 低位
        }
        return byteArray
    }

    private fun shortToByteArray(value: Short): ByteArray {
        val buffer = ByteBuffer.allocate(2)
        buffer.putShort(value)
        return buffer.array()
    }

    private fun bytesToHex(bytes: ByteArray): String {
        return bytes.joinToString(separator = " ") { "%02x".format(it) }
    }

    private fun hexToDecimal(hex: String): Int {
        return Integer.parseInt(hex, 16)
    }

    private fun toHexString(value: Int, digits: Int = 4): String {
        val hexValue = value.toString(16)
        return when {
            hexValue.length < digits -> "0".repeat(digits - hexValue.length) + hexValue
            else -> hexValue
        }
    }
}