package com.example.arifitna.ui.signIn.signInFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arifitna.use_case.InitDatabaseUseCase
import kotlinx.coroutines.launch

class SignInViewModel(
    private val initDatabaseUseCase: InitDatabaseUseCase
) : ViewModel() {

    private val _signInMutableLiveData = MutableLiveData<String>()
    val signInLiveData: LiveData<String> = _signInMutableLiveData

    private val _nowFragment = MutableLiveData<String>("signIn")
    val nowFragment: LiveData<String> = _nowFragment

    fun initDatabase(
        inputEmail: String,
        inputPassword: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        viewModelScope.launch {
            initDatabaseUseCase.execute(
                inputEmail = inputEmail,
                inputPassword = inputPassword,
                onSuccess = {
                    onSuccess()
                },
                onFail = {
                    _signInMutableLiveData.value = "Проблемы при авторизации"
                    onFail()
                }

            )
        }

    }


    fun signIn(inputEmail: String, inputPassword: String) {
        if (inputEmail.isNotEmpty() && inputPassword.isNotEmpty()) {
            initDatabase(
                inputEmail = inputEmail,
                inputPassword = inputPassword,
                onSuccess = {
                    _nowFragment.value = "lkFragment"
                },
                onFail = {
                    _nowFragment.value = "reqistration"
                }
            )
        } else {
            _signInMutableLiveData.value = "Неверный логин или пароль"
        }
    }
}