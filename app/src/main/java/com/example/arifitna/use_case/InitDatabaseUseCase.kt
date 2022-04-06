package com.example.arifitna.use_case

import android.service.autofill.UserData
import android.util.Log
import com.example.arifitna.model.UserStorage
import com.example.arifitna.model.firebase.FirebaseRepository
import com.example.arifitna.util.Constants.REF_DATABASE

class InitDatabaseUseCase(
    private val repository: FirebaseRepository
) {
    fun execute(inputEmail: String, inputPassword: String, onSuccess: () -> Unit, onFail: () -> Unit)  {
        repository.connectToDatabase(
            inputEmail = inputEmail,
            inputPassword = inputPassword,
            onSuccess  = {
                onSuccess()
            },
            onFail = {
                Log.d("VALUE", "eeee")
                onFail()
            }
        )
    }
    fun registration(inputEmail: String, inputPassword: String, userData: UserStorage, onSuccess: () -> Unit, onFail: () -> Unit) {
        repository.registration(
            inputEmail = inputEmail,
            inputPassword = inputPassword,
            userData = userData,
            onSuccess = {
                onSuccess()
            },
            onFail = {
                onSuccess()
            }
        )

    }

}