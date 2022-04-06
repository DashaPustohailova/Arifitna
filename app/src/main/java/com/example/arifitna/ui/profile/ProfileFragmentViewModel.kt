package com.example.arifitna.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arifitna.use_case.AlarmUseCase
import com.example.arifitna.use_case.GetPendingIntUseCase
import com.example.arifitna.use_case.SignOutUseCase
import com.example.focusstart.model.room.dto.PendingInt
import kotlinx.coroutines.launch

class ProfileFragmentViewModel(
    private val signOutUseCase: SignOutUseCase,
    private val alarmUseCase: AlarmUseCase,
    private val getPendingIntUseCase: GetPendingIntUseCase
): ViewModel() {

    private val _pendingIntList = MutableLiveData<List<PendingInt>>()
    var pendingIntList: LiveData<List<PendingInt>> = _pendingIntList

    fun signOut(){
        signOutUseCase.execute()
        deleteNotification()
    }

    fun loadPendingInt() {
        viewModelScope.launch {
            _pendingIntList.value = alarmUseCase.loadPendingInt()
        }
        Log.d("Test", pendingIntList.toString())
    }

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
}