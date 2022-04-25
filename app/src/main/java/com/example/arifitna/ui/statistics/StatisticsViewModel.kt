package com.example.arifitna.ui.statistics

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.arifitna.use_case.AllReportUseCase
import com.example.arifitna.use_case.GetUserDataUseCase

class StatisticsViewModel(
    private val allReportUseCase: AllReportUseCase,
    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel() {

    var userData = getUserDataUseCase.execute()
    var allReport = allReportUseCase.execute()

    fun updateUserData() {
        userData = getUserDataUseCase.execute()
    }

    fun updateReports() {
        allReport = allReportUseCase.execute()
    }
}