package com.example.arifitna.ui.statistics

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arifitna.use_case.AllReportUseCase
import com.example.arifitna.use_case.GetUserDataUseCase

class StatisticsViewModel(
    private val allReportUseCase: AllReportUseCase,
    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel() {

    var userData = getUserDataUseCase.execute()
    var allReport = allReportUseCase.execute()
    var countDay = 0.0
    var count = 0.0
    var normWater = 0.0
    var sred = 0.0
    var percentDay = MutableLiveData<Int>(0)
    var sredWater = MutableLiveData<Int>(0)
    var sredPercent = MutableLiveData<Int>(0)


    fun updateUserData() {
        userData = getUserDataUseCase.execute()
    }

    fun persentDay() {
        if (count > 0.0) {
            if (count < countDay) {
                percentDay.value = Math.round(count / (countDay.toDouble() / 100)).toInt()
            } else {
                percentDay.value = 100
            }
        } else
            percentDay.value = 0
    }

    fun persentSred() {
        if (sred > 0.0) {
            Log.d("sred1",sred.toString() + " "+ normWater )
            if (sred < normWater) {
                sredPercent.value = Math.round(sred / (normWater.toDouble() / 100)).toInt()
            } else {
                sredPercent.value = 100
            }
        } else
            sredPercent.value = 0
    }

    fun updateReports() {
        allReport = allReportUseCase.execute()
    }
}