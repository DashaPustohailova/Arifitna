package com.example.arifitna.ui.signIn.registrationFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arifitna.model.UserStorage
import com.example.arifitna.use_case.InitDatabaseUseCase
import kotlinx.coroutines.launch

class RegistrationFragmentViewModel(
    private val initDatabaseUseCase: InitDatabaseUseCase
) : ViewModel() {
    private val _registrationResult = MutableLiveData<String>("signIn")
    val registrationResult: LiveData<String> = _registrationResult


    var gender =""
    fun registration(
        inputEmail: String,
        inputPassword: String,
        secondPassword: String,
        userData: UserStorage
    ) {
        if (inputPassword.equals(secondPassword)) {

            viewModelScope.launch {
                initDatabaseUseCase.registration(
                    inputEmail = inputEmail,
                    inputPassword = inputPassword,
                    userData = userData,
                    onSuccess = {
                        _registrationResult.value = "success"
                    },
                    onFail = {
                        _registrationResult.value = "fail"
                    }
                )
            }
        }
    }
}