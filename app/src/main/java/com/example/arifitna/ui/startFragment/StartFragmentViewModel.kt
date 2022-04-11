package com.example.arifitna.ui.startFragment

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.arifitna.model.Report
import com.example.arifitna.use_case.*
import java.text.SimpleDateFormat
import java.util.*

class StartFragmentViewModel(
    private val createReportUseCase: CreateReportUseCase,
    private val initBaseData: InitBaseDataUseCase,
    private val currentDataReportLiveData: GetCurrentReportUseCase,
    private val lastReportUseCase: GetLastReportUseCase,
    private val updateCountWaterUseCase: UpdateCountWaterUseCase
) : ViewModel(){

    var currentReport = currentDataReportLiveData.execute()
    var lastReport = lastReportUseCase.execute()
    val sdf = SimpleDateFormat("dd.M.yyyy")

    fun createReport(data: Long) {
        if (data == 0L) {
            //если в базе данных еще нет записи с текущей датой
            createReportUseCase.execute()
            Log.d("Test", "work new report")
        }
        else {
            Log.d("Test", "not work")
        }
    }

    fun updateCurrentReport(){
        currentReport = currentDataReportLiveData.execute()
    }

    fun initBaseData(current_id: String) {
        initBaseData.execute(current_id)
    }

    fun updateLastReport() {
        lastReport = lastReportUseCase.execute()
    }

    fun changeCountWater(countWater: String) {
        updateCountWaterUseCase.execute(Report(sdf.format(Date()),countWater))
    }
}