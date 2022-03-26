package com.example.arifitna.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.example.arifitna.receiver.AlarmReceiver
import com.example.arifitna.util.Constants
import com.example.arifitna.util.RandomIntUtil
import java.util.concurrent.TimeUnit
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.arifitna.R
import com.example.arifitna.ui.MainActivity

class AlarmService(private val context: Context) {
    private var time: Long?= null
    private val alarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?


    fun setExactAlarm(timeInMillis: Long){
        setAlarm(
            timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = Constants.ACTION_SET_EXACT_ALARM
                    putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                }
            )
        )
    }

    //every  minute
    fun setRepetitiveAlarm(timeInMillis: Long, intervalTime: Long = 1L, count: Long = 1L){
        time = timeInMillis
        if(count == 1L){
            setAlarm(
                timeInMillis,
                getPendingIntent(
                    getIntent().apply {
                        action = Constants.ACTION_SET_REPETITIVE_ALARM
                        putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                    }
                )
            )
        }
        else{
            var time = timeInMillis
            for(i in 1L..count){
                setAlarm(
                    time,
                    getPendingIntent(
                        getIntent().apply {
                            action = Constants.ACTION_SET_REPETITIVE_ALARM
                            putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                        }
                    )
                )
                time += TimeUnit.MINUTES.toMillis(intervalTime)
            }
        }
    }

    private fun setAlarm(timeInMillis: Long, pendingIntent: PendingIntent){
        alarmManager?.let {
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            }
            else{
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            }
        }
    }

    fun deleteNotification(){
        alarmManager?.cancel(getPendingIntent(
            getIntent().apply {
                action = Constants.ACTION_SET_REPETITIVE_ALARM
                putExtra(Constants.EXTRA_EXACT_ALARM_TIME, time)
            }
        )
        )
    }

    private fun getIntent() = Intent(context, AlarmReceiver::class.java)

    private fun getPendingIntent(intent: Intent) : PendingIntent{
        var randomInt =
            RandomIntUtil.getRandomInt()
        return  PendingIntent.getBroadcast(
            context,
            randomInt,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }
}