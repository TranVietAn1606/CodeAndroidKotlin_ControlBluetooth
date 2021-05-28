package com.vku.tvan.blutoothcarcontrol

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.rgb
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException

@Suppress("DEPRECATION")
class ControlActivity: AppCompatActivity() {

    var up = 0
    var down = 0
    var left = 0
    var right = 0
    var lampValue = 1
    var hornValue = 1
    var stopValue = 1

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.control_main)
        if(intent != null){
            val name = intent.getStringExtra("nameDevices")
            findViewById<TextView>(R.id.nameConnectTV).text = name
        }

         val stateImg = findViewById<ImageButton>(R.id.stateBtn)
        if (ConnectBluetooth.isConnected == true){
            stateImg.setImageResource(R.drawable.accept)
        } else {
            stateImg.setImageResource(R.drawable.close)
        }
        val lampIB = findViewById<ImageButton>(R.id.lampIB)
        val hornIB = findViewById<ImageButton>(R.id.hornIB)
        val stopIB = findViewById<ImageButton>(R.id.stopIB)
        val upBtn = findViewById<ImageButton>(R.id.upBtn)
        val downBtn = findViewById<ImageButton>(R.id.downBtn)
        val leftBtn = findViewById<ImageButton>(R.id.leftBtn)
        val rightBtn = findViewById<ImageButton>(R.id.rightBtn)
        val disconnectBtn = findViewById<Button>(R.id.disconnectBtn)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)

        lampIB.setOnClickListener {
            lampValue++
            if (lampValue % 2 == 0) {
                lampIB.setBackgroundColor(Color.TRANSPARENT)
                sendCommand("W")
            } else {
                lampIB.setBackgroundColor(Color.LTGRAY)
                sendCommand("w")

            }
        }
        hornIB.setOnClickListener{
            hornValue++
            if(hornValue % 2 == 0){
                hornIB.setBackgroundColor(Color.TRANSPARENT)
                sendCommand("V")
            }else {
                hornIB.setBackgroundColor(Color.LTGRAY)
                sendCommand("v")
            }

        }
        stopIB.setOnClickListener{//S
            stopValue++
            if(stopValue % 2 == 0){
                stopIB.setBackgroundColor(Color.TRANSPARENT)
                sendCommand("S")
            }else {
                stopIB.setBackgroundColor(Color.LTGRAY)
            }

        }

        seekBar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = findViewById<TextView>(R.id.speedTV)
                if(progress < 10) {
                    value.text = "0"
                    sendCommand("0")
                }
                else if(progress == 100) {
                    value.text = "10"
                    sendCommand("q")
                }
                else if(progress >= 10 && progress < 20) {
                    value.text = "1"
                    sendCommand("1")
                }
                else if(progress >= 20 && progress < 30) {
                    value.text = "2"
                    sendCommand("2")
                }
                else if(progress >= 30 && progress < 40) {
                    value.text = "3"
                    sendCommand("3")
                }
                else if(progress >= 40 && progress < 50) {
                    value.text = "4"
                    sendCommand("4")
                }
                else if(progress >= 50 && progress < 60) {
                    value.text = "5"
                    sendCommand("5")
                }
                else if(progress >= 60 && progress < 70) {
                    value.text = "6"
                    sendCommand("6")
                }
                else if(progress >= 70 && progress < 80) {
                    value.text = "7"
                    sendCommand("7")
                }
                else if(progress >= 80 && progress < 90) {
                    value.text = "8"
                    sendCommand("8")
                }
                else if(progress >= 90 && progress < 100) {
                    value.text = "9"
                    sendCommand("9")
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })


        upBtn.setOnTouchListener { v, event ->
            val click = event.action
            when(click){
                MotionEvent.ACTION_DOWN -> {
                    up = 1
                    tb()
                }
                MotionEvent.ACTION_UP -> {
                    up = 0
                    tb()
                }
            }
            true
        }
        downBtn.setOnTouchListener { v, event ->
            val click = event.action
            when(click){
                MotionEvent.ACTION_DOWN -> {
                    down = 1
                    tb()
                }
                MotionEvent.ACTION_UP -> {
                    down = 0
                    tb()
                }
            }
            true
        }

        leftBtn.setOnTouchListener { v, event ->
            val click = event.action
            when(click){
                MotionEvent.ACTION_DOWN -> {
                    left = 1
                    tb()
                }
                MotionEvent.ACTION_UP -> {
                    left = 0
                    tb()
                }
            }
            true
        }

        rightBtn.setOnTouchListener { v, event ->
            val click = event.action
            when(click){
                MotionEvent.ACTION_DOWN -> {
                    right = 1
                    tb()
                }
                MotionEvent.ACTION_UP -> {
                    right = 0
                    tb()
                }
            }
            true
        }

        disconnectBtn.setOnClickListener{
            disconnectBLT()
            onBackPressed()
        }
    }

    private fun disconnectBLT() {
        if (ConnectBluetooth.bluetoothSocket != null) {
            try {
                ConnectBluetooth.bluetoothSocket!!.close()
                ConnectBluetooth.bluetoothSocket = null
                ConnectBluetooth.isConnected = false
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        finish()
    }
    private fun sendCommand(output: String) {
        if (ConnectBluetooth.bluetoothSocket != null) {
            try{
                ConnectBluetooth.bluetoothSocket!!.outputStream.write(output.toByteArray())
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun tb(){
        val img = findViewById<ImageView>(R.id.imgControl)
        if(up == 1) {
            img.setImageResource(R.drawable.up_control)
            sendCommand("F")
        }
        if(down == 1)  {
            img.setImageResource(R.drawable.down_control)
            sendCommand("B")
        }
        if(left == 1)  {
            img.setImageResource(R.drawable.left_control)
            sendCommand("L")
        }
        if(right == 1)  {
            img.setImageResource(R.drawable.right_control)
            sendCommand("R")
        }
        if(up == 1 && left == 1) {
            img.setImageResource(R.drawable.up_left_control)
            sendCommand("G")
        }
        if(up == 1 && right == 1) {
            img.setImageResource(R.drawable.up_right_control)
            sendCommand("I")
        }
        if(down == 1 && left == 1) {
            img.setImageResource(R.drawable.down_left_control)
            sendCommand("H")
        }
        if(down == 1 && right == 1) {
            img.setImageResource(R.drawable.down_right_control)
            sendCommand("J")
        }
        if(up == 0 && down == 0 && left == 0 && right == 0 )  {
            img.setImageResource(R.drawable.stop_control)
            sendCommand("S")
        }
    }

}

