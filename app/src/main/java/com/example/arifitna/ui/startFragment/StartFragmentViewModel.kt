package com.example.arifitna.ui.startFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arifitna.model.Report
import com.example.arifitna.use_case.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class StartFragmentViewModel(
    private val createReportUseCase: CreateReportUseCase,
    private val initBaseData: InitBaseDataUseCase,
    private val currentDataReportLiveData: GetCurrentReportUseCase,
    private val lastReportUseCase: GetLastReportUseCase,
    private val updateCountWaterUseCase: UpdateCountWaterUseCase,
    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel() {

    val sdf = SimpleDateFormat("dd.M.yyyy")
    var percent = MutableLiveData<Int>(0)
    var normWater = 1.1
    var partWater = 0.0

    fun lastPersent() {
        if (partWater > 0.0) {
            if (partWater < normWater) {
                percent.value = Math.round(partWater / (normWater.toDouble() / 100)).toInt()
            } else {
                percent.value = 100
            }
        } else
            percent.value = 0
    }

    var userDataLiveData = getUserDataUseCase.execute()
    fun updateUserData() {
        userDataLiveData = getUserDataUseCase.execute()
    }

    var currentReport = currentDataReportLiveData.execute()
    fun updateCurrentReport() {
        currentReport = currentDataReportLiveData.execute()
    }

    var lastReport = lastReportUseCase.execute()
    fun updateLastReport() {
        lastReport = lastReportUseCase.execute()
    }

    fun createReport(data: Long) {
        if (data == 0L) {
            //если в базе данных еще нет записи с текущей датой
            createReportUseCase.execute()
        } else {
        }
    }

    fun initBaseData(current_id: String) {
        initBaseData.execute(current_id)
    }

    fun changeCountWater(countWater: String) {
        var result = lastReport.value?.let {
            it.water.toInt() + countWater.toInt()
        }
        viewModelScope.launch {
            updateCountWaterUseCase.execute(Report(sdf.format(Date()), result.toString()))
        }
    }

}