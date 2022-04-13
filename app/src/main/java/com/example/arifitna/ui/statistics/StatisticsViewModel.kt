package com.example.arifitna.ui.statistics

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.arifitna.use_case.AllReportUseCase
import com.example.arifitna.use_case.GetUserDataUseCase

class StatisticsViewModel(
    private val allReportUseCase: AllReportUseCase,
    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel(){

    val userData = getUserDataUseCase.execute()
    val allReport = allReportUseCase.execute()

    fun updateUserData(){
        getUserDataUseCase.execute()
        Log.d("stat", userData.value.toString())
    }

    fun updateReports(){
        allReportUseCase.execute()
        Log.d("stat", allReport.value.toString())
    }
}