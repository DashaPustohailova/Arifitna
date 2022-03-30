package com.example.arifitna.ui.Settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arifitna.use_case.AlarmUseCase
import com.example.arifitna.use_case.GetPendingIntUseCase
import com.example.arifitna.use_case.SavePendingIntUseCase
import com.example.focusstart.model.room.dto.PendingInt
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SettingsViewModel(
private val alarmUseCase: AlarmUseCase,
private val getPendingIntUseCase: GetPendingIntUseCase,
private val savePendingIntUseCase: SavePendingIntUseCase
) : ViewModel(){

    private val _pendingIntList = MutableLiveData<List<PendingInt>>()
    var pendingIntList: LiveData<List<PendingInt>> = _pendingIntList

    fun deleteNotification() {
        pendingIntList.value?.let {
            Log.d("Test", "deleteNotification viewModel" + it)
            alarmUseCase.deleteNotification(it)
        }
        viewModelScope.launch {
            getPendingIntUseCase.deletePendingInt()
            _pendingIntList.value = alarmUseCase.loadPendingInt()
            Log.d("Test", "deleteNotification viewModel after delete" + _pendingIntList.value.toString())
        }
    }

    fun setRepetitiveAlarm(startTimeAlarm: Long, endTimeAlarm: Long, intervalTime: Long) {

//        viewModelScope.launch {
//            deleteNotification()
//        }

        viewModelScope.launch {
            var count =
                ((endTimeAlarm!! - startTimeAlarm!!) / TimeUnit.MINUTES.toMillis(intervalTime)) + 1L
            var list = alarmUseCase.setRepetitiveAlarm(
                startTimeAlarm!!,
                intervalTime,
                count
            )
            for(id in list){
                savePendingIntUseCase.save(id)
            }
            _pendingIntList.value = getPendingIntUseCase.getPendingInt()
            Log.d("Test", _pendingIntList.toString())
        }



    }

    fun loadPendingInt() {
        viewModelScope.launch {
            _pendingIntList.value = alarmUseCase.loadPendingInt()
        }
        Log.d("Test", pendingIntList.toString())
    }
}