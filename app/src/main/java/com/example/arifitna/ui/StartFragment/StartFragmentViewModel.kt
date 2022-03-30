package com.example.arifitna.ui.StartFragment

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

class StartFragmentViewModel() : ViewModel(){}