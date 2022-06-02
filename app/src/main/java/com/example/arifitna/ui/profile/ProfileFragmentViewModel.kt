package com.example.arifitna.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arifitna.model.Report
import com.example.arifitna.model.UserStorage
import com.example.arifitna.model.room.dto.PendingInt
import com.example.arifitna.use_case.*
import kotlinx.android.synthetic.main.fragment_start.*
import kotlinx.coroutines.launch
import java.util.*

class ProfileFragmentViewModel(
    private val signOutUseCase: SignOutUseCase,
    private val alarmUseCase: AlarmUseCase,
    private val getPendingIntUseCase: GetPendingIntUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val updatePersonalDataUseCase: UpdatePersonalDataUseCase,
    private val getUserPermission: GetUserPermission,
    private val getPrice: GetPrice,
    private val addBonusUseCase: AddBonusUseCase,
    private val updatePermissionsUseCase: UpdatePermissionsUseCase
) : ViewModel() {


    private val _pendingIntList = MutableLiveData<List<PendingInt>>()
    var pendingIntList: LiveData<List<PendingInt>> = _pendingIntList
    var message: MutableLiveData<String> = MutableLiveData<String>("")

    var userDataLiveData = getUserDataUseCase.execute()
    var userPermission = getUserPermission.execute()
    var price = getPrice.execute()


    fun pay(price: Int){
        var result = userDataLiveData.value?.let {
            it.bonus - price
        }
        viewModelScope.launch {
            addBonusUseCase.execute(result?:0)
        }
    }

    fun payToPermissionSuccess(permission: String){
        viewModelScope.launch {
            updatePermissionsUseCase.execute(permission)
        }
    }

    fun updateUserData() {
        userPermission = getUserPermission.execute()
    }

    fun updateUserPermission() {
        userPermission = getUserPermission.execute()


    }
    fun updatePrice() {
        price = getPrice.execute()
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase.execute()
        }
        deleteNotification()
    }

    fun loadPendingInt() {
        viewModelScope.launch {
            _pendingIntList.value = alarmUseCase.loadPendingInt()
        }
    }

    fun deleteNotification() {
        pendingIntList.value?.let {
            alarmUseCase.deleteNotification(it)
        }
        viewModelScope.launch {
            getPendingIntUseCase.deletePendingInt()
            _pendingIntList.value = alarmUseCase.loadPendingInt()
        }
    }

    fun updatePersonalData(name: String, weight: String) {
        viewModelScope.launch {
            updatePersonalDataUseCase.execute(
                userDataLiveData.value?: UserStorage(),
                name,
                weight,
                {
//                    message.value = "Данные обновлены"
                },
                {
                    message.value = "Ошибка сохранения данных"
                })
        }
    }
}