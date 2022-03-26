package com.example.arifitna.ui

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.arifitna.R
import com.example.arifitna.service.AlarmService
import kotlinx.android.synthetic.main.fragment_start.*
import java.util.*
import java.util.concurrent.TimeUnit

class StartFragment : Fragment(R.layout.fragment_start) {
    lateinit var alarmService: AlarmService
    private var startTimeAlarm:  Long ?= null
    private var endTimeAlarm:  Long ?= null
    private var intervalTime:  Long  = 1L

//    private fun setAlarm(callback: (Long) -> Unit){
    private fun setAlarm(flag: Int){
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
                TimePickerDialog(
                    requireContext(),
                    0,
                    TimePickerDialog.OnTimeSetListener { _, hourOfDay, min ->
                        this.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        this.set(Calendar.MINUTE, min)
                        if(flag == 0){
                            startTimeAlarm = this.timeInMillis
                        }
                        else if(flag == 1){
                            endTimeAlarm = this.timeInMillis
                        }
                    },
                    this.get(Calendar.HOUR_OF_DAY),
                    this.get(Calendar.MINUTE),
                    true
                ).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initService()
        setupClickListeners()
        setupSpinner()
    }

    private fun initService() {
        alarmService = AlarmService(requireContext())
    }

//    private fun deleteNotification(){
//        alarmService.deleteNotification()
//    }

    private fun setupClickListeners() {
        startTime.setOnClickListener {
            setAlarm(0)
        }
        stopTime.setOnClickListener {
            setAlarm(1)
        }

        startButton.setOnClickListener{
            Log.d("Test", "Start time" + startTimeAlarm.toString())
            startTimeAlarm?.let {
                Log.d("Test", startTimeAlarm.toString())
                endTimeAlarm?.let {
                    Log.d("Test", endTimeAlarm.toString() + " " + ((endTimeAlarm!!-startTimeAlarm!!) / TimeUnit.MINUTES.toMillis(intervalTime)))
                    alarmService.setRepetitiveAlarm(startTimeAlarm!!, intervalTime,((endTimeAlarm!!-startTimeAlarm!!) / TimeUnit.MINUTES.toMillis(intervalTime)) + 1L)
                }
            }

//            setAlarm {timeInMillis ->
//                val simpleDateFormat = SimpleDateFormat("H")
//                val dateString = simpleDateFormat.format(timeInMillis)
//                Log.d("Time", dateString)
//                alarmService.setRepetitiveAlarm(timeInMillis, 2)  }
//            setAlarm{timeInMillis -> alarmService.setExactAlarm(timeInMillis)}
//            Intent(requireContext(), MyService::class.java).also {
//                requireActivity().startService(it)
//            }
        }
//        stopButton.setOnClickListener {
//            deleteNotification()
//              Intent(requireContext(), MyService::class.java).also {
//                 MyService.stopService()
//              }
//        }

    }
    private fun setupSpinner() {
        var  array = arrayListOf<String>("1 минута", "2 минуты", "30 минут", "1 час", "2 часа", "3 часа")
        var adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            array)
        spinnerCurrency.adapter = adapter
        spinnerCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                position: Int,
                p3: Long
            ) {
                when (position){
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