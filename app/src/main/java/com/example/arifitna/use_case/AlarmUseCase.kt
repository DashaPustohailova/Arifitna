package com.example.arifitna.use_case

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.arifitna.receiver.AlarmReceiver
import com.example.arifitna.util.Constants
import com.example.arifitna.util.RandomIntUtil
import com.example.focusstart.model.room.dto.PendingInt
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AlarmUseCase(
    private val context: Context,
//    private val savePendingInt: SavePendingIntUseCase,
    private val getPendingIntUseCase: GetPendingIntUseCase
) {
    private var time: Long? = null
    private val alarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
//    private var randomInt = 0
    private var listRandomInt = mutableListOf<Int>()

//    private fun saveRandomInt(randomInt: Int) {
//        GlobalScope.launch(Dispatchers.Main) {
//            savePendingInt.save(randomInt)
//        }
//    }

    fun setRepetitiveAlarm(timeInMillis: Long, intervalTime: Long = 1L, count: Long = 1L) : List<Int>{
        time = timeInMillis
        if (count == 1L) {
            setAlarm(
                timeInMillis,
                getPendingIntent(
                    getIntent().apply {
                        action = Constants.ACTION_SET_REPETITIVE_ALARM
                        putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                    }
                )
            )
        } else {
            var time = timeInMillis
            for (i in 1L..count) {
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
        Log.d("Test", "setRepetitiveAlarm in UseCase" + listRandomInt)
        return listRandomInt
    }

    private fun setAlarm(timeInMillis: Long, pendingIntent: PendingIntent) {
        alarmManager?.let {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            }
        }
    }

    fun deleteNotification(list: List<PendingInt>) {
        list?.let {
            for (randomInt in it) {
                alarmManager?.cancel(
                    getPendingIntentToCancel(
                        getIntent().apply {
                            action = Constants.ACTION_SET_REPETITIVE_ALARM
                            putExtra(Constants.EXTRA_EXACT_ALARM_TIME, time)
                        },
                        randomInt.id
                    )
                )
            }
        }
    }

    private fun getIntent() = Intent(context, AlarmReceiver::class.java)

    private fun getPendingIntent(intent: Intent): PendingIntent {
        var randomInt =
            RandomIntUtil.getRandomInt()
        listRandomInt.add(randomInt)
        return PendingIntent.getBroadcast(
            context,
            randomInt,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getPendingIntentToCancel(intent: Intent, randomInt: Int) : PendingIntent  =
          PendingIntent.getBroadcast(
        context,
        randomInt,
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )

    suspend fun loadPendingInt() = getPendingIntUseCase.getPendingInt()

    suspend  fun deleteAll(){
       getPendingIntUseCase.deletePendingInt()
    }

}