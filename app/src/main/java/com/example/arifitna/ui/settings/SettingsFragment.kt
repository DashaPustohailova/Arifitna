package com.example.arifitna.ui.settings

import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.arifitna.R
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val viewModel by viewModel<SettingsViewModel>()
    private var startTimeAlarm: Long? = null
    private var endTimeAlarm: Long? = null
    private var intervalTime: Long = 1L

    private val sharedPreferences: SharedPreferences by inject<SharedPreferences>()
    private val sharedPreferencesEditor: SharedPreferences.Editor by inject<SharedPreferences.Editor>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadPendingInt()
        setupClickListeners()
        setupSpinner()
        var start =sharedPreferences.getLong("START_TIME", 0L)
        var end = sharedPreferences.getLong("END_TIME", 0L)
        if(start != 0L && end != 0L ){
            startTime.text =
                DateFormat.format("hh:mm", sharedPreferences.getLong("START_TIME", 0L)).toString()
            stopTime.text =
                DateFormat.format("hh:mm", sharedPreferences.getLong("END_TIME", 0L) ).toString()
            startButton.isEnabled = false
            stopTime.isEnabled = false
            startTime.isEnabled = false
            stopButton.isEnabled = true
        }
        else{
            startButton.isEnabled = true
            stopTime.isEnabled = true
            startTime.isEnabled = true
            stopButton.isEnabled = false
        }
    }

    private fun setAlarm(flag: Int) {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
            TimePickerDialog(
                requireContext(),
                0,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, min ->
                    this.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    this.set(Calendar.MINUTE, min)
                    if (flag == 0) {
                        startTimeAlarm = this.timeInMillis
                        startTime.text =
                            DateFormat.format("hh:mm", this.timeInMillis).toString()
                        sharedPreferencesEditor.apply {
                            putLong("START_TIME", startTimeAlarm!!)
                            apply()
                        }
                    } else if (flag == 1) {
                        endTimeAlarm = this.timeInMillis
                        stopTime.text =
                            DateFormat.format("hh:mm", this.timeInMillis).toString()
                        sharedPreferencesEditor.apply {
                            putLong("END_TIME", endTimeAlarm!!)
                            apply()
                        }
                    }
                },
                this.get(Calendar.HOUR_OF_DAY),
                this.get(Calendar.MINUTE),
                true
            ).show()
        }
    }


    private fun setupClickListeners() {
        startTime.setOnClickListener {
            setAlarm(0)
        }
        stopTime.setOnClickListener {
            setAlarm(1)
        }

        startButton.setOnClickListener {
            startTimeAlarm?.let { startTimeAlarm ->
                endTimeAlarm?.let { endTimeAlarm ->
                    viewModel.setRepetitiveAlarm(startTimeAlarm, endTimeAlarm, intervalTime)
                }
            }
            startButton.isEnabled = false
            stopTime.isEnabled = false
            startTime.isEnabled = false
            stopButton.isEnabled = true
        }
        stopButton.setOnClickListener {
            viewModel.deleteNotification()
            startButton.isEnabled = true
            stopTime.isEnabled = true
            startTime.isEnabled = true
            stopButton.isEnabled = false
            sharedPreferencesEditor.apply {
                putLong("END_TIME", 0L)
                putLong("START_TIME", 0L)
                apply()
            }
            startTime.text = "Установить"
            stopTime.text = "Установить"
        }
    }

    private fun setupSpinner() {
        var array =
            arrayListOf<String>("1 минута", "2 минуты", "30 минут", "1 час", "2 часа", "3 часа")
        var adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            array
        )
        spinnerCurrency.adapter = adapter
        spinnerCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                position: Int,
                p3: Long
            ) {
                when (position) {
                    0 -> {
                        intervalTime = 1
                    }
                    1 -> {
                        intervalTime = 2
                    }
                    2 -> {
                        intervalTime = 30
                    }
                    3 -> {
                        intervalTime = 60
                    }
                    4 -> {
                        intervalTime = 120
                    }
                    5 -> {
                        intervalTime = 180
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }
}