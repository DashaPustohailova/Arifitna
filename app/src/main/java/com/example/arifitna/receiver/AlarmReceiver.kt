package com.example.arifitna.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import com.example.arifitna.use_case.AlarmUseCase
import com.example.arifitna.util.Constants
import io.karn.notify.Notify
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmReceiver() : BroadcastReceiver() {

    private val alarmUseCase: AlarmUseCase by inject<AlarmUseCase>(AlarmUseCase::class.java)

    override fun onReceive(context: Context, intent: Intent) {
        val timeInMillis = intent.getLongExtra(Constants.EXTRA_EXACT_ALARM_TIME, 0L)
        when (intent.action) {
            Constants.ACTION_SET_EXACT_ALARM -> {
                buildNotification(context, "Set Exact Time", convertDate(timeInMillis))
            }
            Constants.ACTION_SET_REPETITIVE_ALARM -> {
                setRepetitiveAlarm()
                buildNotification(context, "Напоминание", convertDate(timeInMillis))
            }
        }
    }

    private fun buildNotification(context: Context, title: String, message: String) {
        Notify
            .with(context)
            .content {
                this.title = title
                text = "Пора выпить воды"
            }
            .show()
    }

    private fun setRepetitiveAlarm() {
        val cal = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis + TimeUnit.HOURS.toMillis(24) // каждый день
            Timber.d("Set alarm for next week same time - ${convertDate(this.timeInMillis)}")
        }
        alarmUseCase.setRepetitiveAlarm(cal.timeInMillis)
    }

    private fun convertDate(timeInMillis: Long): String =
        DateFormat.format("dd/MM/yyyy hh:mm:ss", timeInMillis).toString()

}