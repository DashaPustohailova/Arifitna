package com.example.arifitna.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.format.DateFormat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.arifitna.R
import com.example.arifitna.ui.MainActivity
import com.example.arifitna.use_case.AlarmUseCase
import com.example.arifitna.util.Constants
import org.koin.java.KoinJavaComponent.inject
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmReceiver() : BroadcastReceiver() {

    lateinit var notificationManager: NotificationManagerCompat
    lateinit var notification: Notification
    val CHANNEL_ID = "channelId"
    val CHANNEL_NAME = "channelName"
    val NOTIFICATION_ID = 888

    private val alarmUseCase: AlarmUseCase by inject<AlarmUseCase>(AlarmUseCase::class.java)

    override fun onReceive(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val timeInMillis = intent.getLongExtra(Constants.EXTRA_EXACT_ALARM_TIME, 0L)
        when (intent.action) {
            Constants.ACTION_SET_EXACT_ALARM -> {
                createNotification(context)
            }
            Constants.ACTION_SET_REPETITIVE_ALARM -> {
                setRepetitiveAlarm()
                createNotification(context)
            }
        }
    }


    private fun createNotification(context: Context) {
        val resultIntent = Intent(context, MainActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(
            context, 0, resultIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Напоминания пить воду")
            .setContentText("Пора выпить воды!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(resultPendingIntent)
            .build()

        notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }


    private fun setRepetitiveAlarm() {
        val cal = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis + TimeUnit.HOURS.toMillis(24) // каждый день
        }
        alarmUseCase.setRepetitiveAlarm(cal.timeInMillis)
    }

    private fun convertDate(timeInMillis: Long): String =
        DateFormat.format("dd/MM/yyyy hh:mm:ss", timeInMillis).toString()

}