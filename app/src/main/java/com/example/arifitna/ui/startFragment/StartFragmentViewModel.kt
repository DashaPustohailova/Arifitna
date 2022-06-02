package com.example.arifitna.ui.startFragment

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arifitna.model.Drinks
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
    private val getUserDataUseCase: GetUserDataUseCase,
    private val addBonusUseCase: AddBonusUseCase,
    private val getAllDrinksUseCase: GetAllDrinksUseCase,
    private val getUserPermission: GetUserPermission
) : ViewModel(){

    val sdf = SimpleDateFormat("dd.M.yyyy")
    var percent = MutableLiveData<Int>(0)
    var normWater = 1.1
    var partWater = 0.0
    var allWater = ""
    var bonus = 0
    var listDrinks = getAllDrinksUseCase.execute()

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

    var userPermission = getUserPermission.execute()

    fun updateUserPermission() {
        userPermission = getUserPermission.execute()
    }

    fun updateListOfDrinks(){
        listDrinks = getAllDrinksUseCase.execute()
    }
    var currentReport = currentDataReportLiveData.execute()
    fun updateCurrentReport() {
        currentReport = currentDataReportLiveData.execute()
    }

    var lastReport = lastReportUseCase.execute()

    fun updateLastReport() {
        lastReport = lastReportUseCase.execute()
        Log.d("bonus", "allWater = " + lastReport.value?.allWater )
        allWater = lastReport.value?.allWater ?: ""
    }

    fun createReport(data: Long) {
        if (data == 0L) {
            //если в базе данных еще нет записи с текущей датой
            createReportUseCase.execute()
        } else {
        }
    }

    fun initBaseData(current_id: String) {
        viewModelScope.launch {
            initBaseData.execute(current_id)
        }
    }

    fun changeCountWater(countWater: String) {
        var result = lastReport.value?.let {
            it.water.toInt() + Math.round(countWater.toDouble())
        }

        viewModelScope.launch {
            Log.d("bonus", " " +result.toString()+ " " + userDataLiveData.value?.normWater  + " " + lastReport.value?.allWater )
            if ( result?:0 >= normWater.toInt() && lastReport.value?.allWater  != "true") {
                Log.d("bonus", "true")
                allWater = "true"
                updateCountWaterUseCase.execute(
                    Report(
                        sdf.format(Date()),
                        result.toString(),
                        "true"
                    )
                )
                Log.d("bonus", "bb" +  userDataLiveData.value?.bonus.toString())
                addBonusUseCase.execute(bonus + 50)
            } else {
                Log.d("bonus", "false")
                updateCountWaterUseCase.execute(
                    Report(
                        sdf.format(Date()),
                        result.toString(),
                        lastReport.value?.allWater.toString()
                    )
                )
            }
        }
    }




}