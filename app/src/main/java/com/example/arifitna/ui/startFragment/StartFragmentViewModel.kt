package com.example.arifitna.ui.startFragment

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.arifitna.use_case.CreateReportUseCase
import com.example.arifitna.use_case.GetCurrentReportUseCase
import com.example.arifitna.use_case.GetLastReportUseCase
import com.example.arifitna.use_case.InitBaseDataUseCase

class StartFragmentViewModel(
    private val createReportUseCase: CreateReportUseCase,
    private val initBaseData: InitBaseDataUseCase,
    private val currentDataReportLiveData: GetCurrentReportUseCase,
    private val lastReportUseCase: GetLastReportUseCase
) : ViewModel(){

    var currentReport = currentDataReportLiveData.execute()
    var lastReport = lastReportUseCase.execute()

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
}