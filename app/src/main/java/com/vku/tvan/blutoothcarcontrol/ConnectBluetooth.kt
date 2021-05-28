package com.vku.tvan.blutoothcarcontrol

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class ConnectBluetooth: AppCompatActivity() {
    val PROFILE_ACTIVITY_CODE = 200
    private var bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val REQUEST_ENABLE_BLUETOOTH = 1
    var value = 0;

    val listDevices = ArrayList<infoBlt>()

    companion object {
        var myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var bluetoothSocket: BluetoothSocket? = null
        lateinit var bluetoothAdapter: BluetoothAdapter
        lateinit var progress: ProgressDialog
        var isConnected: Boolean = false
        lateinit var addressDevice: String
    }

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listdevice_main)


        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Thiet bi khong ho tro BLUETOORH", Toast.LENGTH_LONG).show()
        }
        // bật blt
        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH)
        }


        val connBtn = findViewById<Button>(R.id.connBtn)
        connBtn.setOnClickListener {
            val listInfo = ArrayList<infoBlt>()
            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
            pairedDevices?.forEach { device ->
                listInfo.add(infoBlt(device.name, device.address))
            }
            val bluetoothRV2 = findViewById<RecyclerView>(R.id.recyclerView2)
            val bluetoothAdapterRV = BluetoothAdapterRV(this, listInfo)

            bluetoothRV2.layoutManager = GridLayoutManager(this,1)
            bluetoothRV2.adapter = bluetoothAdapterRV

            bluetoothAdapterRV.onItemClick={ list->
                addressDevice = list.address
                connectBLT(addressDevice)
                val controlIntent = Intent(this, ControlActivity::class.java)
                controlIntent.putExtra("nameDevices", list.name)
                controlIntent.putExtra("addressDevices", list.address)
                startActivityForResult(controlIntent,PROFILE_ACTIVITY_CODE)
            }
        }


        val scanBtn = findViewById<Button>(R.id.scanBtn)
        scanBtn.setOnClickListener{
            val requestCode = 1;
            val discoverableIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
            }
            startActivityForResult(discoverableIntent, requestCode)

            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(receiver, filter)
            bluetoothAdapter?.startDiscovery()
            val bluetoothRV1 = findViewById<RecyclerView>(R.id.recyclerView1)
            val bluetoothAdapterRV = BluetoothAdapterRV(this, listDevices)

            bluetoothRV1.layoutManager = GridLayoutManager(this,1)
            bluetoothRV1.adapter = bluetoothAdapterRV

            bluetoothAdapterRV.onItemClick={ list->
                addressDevice = list.address
                connectBLT(addressDevice)
            val controlIntent = Intent(this, ControlActivity::class.java)
            controlIntent.putExtra("nameDevices", list.name)
            controlIntent.putExtra("addressDevices", list.address)
            startActivityForResult(controlIntent,PROFILE_ACTIVITY_CODE)
            }
        }


    }

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (device != null) {
                        listDevices.add(infoBlt(device.name, device.address))
                        Log.d("name", device.name)
                    }
                }
            }
        }
    }

/*
override fun onDestroy() {
super.onDestroy()
unregisterReceiver(receiver)
}
*/


    fun connectBLT(s : String) {
        var connectSuccess: Boolean = true
        try {
            if (bluetoothSocket == null || !isConnected) {
                Companion.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                val device: BluetoothDevice = Companion.bluetoothAdapter.getRemoteDevice(s)
                bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(myUUID)
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                bluetoothSocket!!.connect()

            }
        } catch (e: IOException) {
            connectSuccess = false
            e.printStackTrace()
        }
        if (!connectSuccess) {
        } else {
            isConnected = true
        }

    }




    private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>() {
        private var connectSuccess: Boolean = true
        private val context: Context

        init {
            this.context = c
        }

        override fun onPreExecute() {
            super.onPreExecute()
            progress = ProgressDialog.show(context, "Connecting...", "please wait")
        }

        override fun doInBackground(vararg p0: Void?): String? {
            try {
                if (bluetoothSocket == null || !isConnected) {
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = bluetoothAdapter.getRemoteDevice(addressDevice)
                    bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(myUUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    bluetoothSocket!!.connect()
                }
            } catch (e: IOException) {
                connectSuccess = false
                Log.d("lỗi: ","khong co thiet bi ")
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!connectSuccess) {
                Log.i("data", "couldn't connect")
            } else {
                isConnected = true
            }
            progress.dismiss()
        }
    }
}


