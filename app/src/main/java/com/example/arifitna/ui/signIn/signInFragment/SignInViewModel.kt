package com.example.arifitna.ui.signIn.signInFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arifitna.use_case.InitDatabaseUseCase

class SignInViewModel(
    private val initDatabaseUseCase: InitDatabaseUseCase
) : ViewModel() {

    private val _signInMutableLiveData = MutableLiveData<String>()
    val signInLiveData : LiveData<String> = _signInMutableLiveData

    private val _nowFragment = MutableLiveData<String>("signIn")
    val nowFragment : LiveData<String> = _nowFragment

    fun initDatabase(inputEmail: String, inputPassword: String, onSuccess: () -> Unit, onFail: () -> Unit) {
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


    fun signIn(inputEmail: String, inputPassword: String) {
        if(inputEmail.isNotEmpty() && inputPassword.isNotEmpty()){
            initDatabase(
                inputEmail = inputEmail,
                inputPassword = inputPassword,
                onSuccess = {
                    _nowFragment.value = "lkFragment"
                    Log.d("Test", "SIGN IN")
                    //если инициализация прошла успешно
//                    APP_ACTIVITY.mNavController.navigate(R.id.action_startFragment_to_lkFragment)
//                    _signInMutableLiveData.value = "Инициализация прошла успешно"
                },
                onFail = {
                    _nowFragment.value = "reqistration"
                    Log.d("Test", "fail")
//                    APP_ACTIVITY.mNavController.navigate(R.id.action_startFragment_to_registrateFragment)
//                    _signInMutableLiveData.value = "Аккаунт не существует"
                }
            )
        }
        else{
            _signInMutableLiveData.value = "Неверный логин или пароль"
        }
    }
}