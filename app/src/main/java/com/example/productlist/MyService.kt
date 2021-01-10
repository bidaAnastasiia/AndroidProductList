package com.example.productlist

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MyService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var chanId = intent!!.getStringExtra("channel_id")
            val chanName = intent.getStringExtra("channel_name")
            if (chanId != null && chanName != null)
                createChannel(chanId, chanName)
            else
                chanId = createChannel("channel_def", "Default channel")

            val mapActivity = Intent()


            val pendingIntent = PendingIntent.getActivity(
                this,
                intent.getIntExtra("id", 0),
                mapActivity,
                PendingIntent.FLAG_ONE_SHOT
            )
            val notification = NotificationCompat.Builder(this,chanId)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Welcome to the shop: ")
                .setContentText(intent.getStringExtra("shopName")+"! Today is "+intent.getStringExtra("sale") +"% sale!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            NotificationManagerCompat.from(this).notify(intent.getIntExtra("id", 0),notification)
        } else{
            //If API<26
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannel( id:String, name:String): String{
        val notificationChannel = NotificationChannel(
            id,
            name,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        NotificationManagerCompat.from(this).createNotificationChannel(notificationChannel)
        return id
    }
}