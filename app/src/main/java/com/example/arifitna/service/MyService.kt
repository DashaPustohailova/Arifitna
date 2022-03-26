package com.example.arifitna.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.arifitna.R
import com.example.arifitna.ui.MainActivity
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class MyService : Service(),  CoroutineScope {
    val TAG = "MyService"
    lateinit var notificationManager: NotificationManagerCompat
    lateinit var notification: Notification
    val CHANNEL_ID =  "channelId"
    val CHANNEL_NAME = "channelName"
    val NOTIFICATION_ID = 888

    init {
        instance = this
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
            val manager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    private fun createNotification() {
        val resultIntent = Intent(this, MainActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(
            this, 0, resultIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Напоминания пить воду")
            .setContentText("Пора выпить воды!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(resultPendingIntent)
            .build()

        notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isRunning = true
        val dataString = intent?.getStringExtra("EXTRA_DATA")
        dataString?.let {
            Log.d(TAG, dataString)
        }


        val sdf = SimpleDateFormat("H")
        var currentDate = sdf.format(Date()) //Час в дне (0-23)

        launch(coroutineContext) {
            while(isRunning  && currentDate > "13" ) {
                    createNotification()
                    delay(60000) // 1 минута
                    currentDate = sdf.format(Date())
                }
            }
        return START_STICKY
    }

    companion object{
        private lateinit var instance: MyService
        var isRunning = false

        fun stopService(){
            Log.d("MyService", "Service is stopping...")
            isRunning = false
            instance.stopSelf()

        }
    }
}