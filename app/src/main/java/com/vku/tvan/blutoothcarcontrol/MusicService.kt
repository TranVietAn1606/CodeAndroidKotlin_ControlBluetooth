package com.vku.tvan.blutoothcarcontrol

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import java.lang.UnsupportedOperationException
import java.util.*

class MusicService : Service(){
    var player: MediaPlayer? = null
    var state = 0
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent != null) {
            state = intent.getIntExtra("state", 0)
        }
        player = MediaPlayer.create(this,R.raw.music)
        checkState()
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }
    fun checkState(){
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object:Runnable{
            override fun run() {
                if(state == 1){}
                else {
                    player?.start()
                }
            }
        })

    }
    override fun onDestroy() {
        player?.stop()
        super.onDestroy()
    }

}
