package com.vku.tvan.blutoothcarcontrol

import android.Manifest
import android.app.Activity
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
private const val PERMISSION_REQUEST_CODE = 10
class MainActivity : AppCompatActivity() {
    private var permissions = arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION)
    private var statusPermissions: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(checkPermissions(this,permissions)){
            statusPermissions = true
        }
        else{
            requestPermissions(permissions, PERMISSION_REQUEST_CODE)
        }

//        var intentMS = Intent(this,MusicService::class.java)
//        startService(intentMS)

        val controlBtn = findViewById<ImageButton>(R.id.control_main_btn)
        val soundBtn = findViewById<ImageButton>(R.id.sound_main_btn)
        val infoBtn = findViewById<ImageButton>(R.id.info_main_btn)

        controlBtn.setOnClickListener {

            val connectItent = Intent(this, ConnectBluetooth::class.java)
            startActivity(connectItent)
        }
        infoBtn.setOnClickListener {
            val infomationIntent = Intent(this, InfomationActivity::class.java)
            startActivity(infomationIntent)
        }
        soundBtn.setOnClickListener(clickListener)
    }

        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.sound_main_btn -> {
                    showPopup(view)
                }
            }
        }

    fun showPopup(v: View) {
        var popup: PopupMenu? = null;
        popup = PopupMenu(this, v)
        popup.inflate(R.menu.sound_popup_menu)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            var intentMS = Intent(this,MusicService::class.java)
            when (item!!.itemId) {
                R.id.off_sound_IT -> {
                    stopService(intentMS)
                }
                R.id.on_sound_IT -> {
                    intentMS.putExtra("state", "1")
                    startService(intentMS)
                }

            }

            true
        })
        popup.show()
    }


    fun checkPermissions(context: Context, permissionArray: Array<String>): Boolean{
        var isAllGranted = true
        for (i in permissionArray.indices){
            if(checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED)
                isAllGranted = false
        }
        return isAllGranted
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_CODE){
            var allGranted = true
            for (i in permissions.indices){
                if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                    allGranted = false
                    var requestAgainLater = shouldShowRequestPermissionRationale(permissions[i])
                    if(requestAgainLater){
                        Toast.makeText(this,"Quyền truy cập đã bị từ chối",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this,"Đi đến phần cài đặt để cấp quyền cho ứng dụng",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if(allGranted){
                Toast.makeText(this,"All permissions granted",Toast.LENGTH_SHORT).show()
            }
        }
    }

}



