package com.example.arifitna.use_case

import androidx.lifecycle.LiveData
import com.example.arifitna.model.UserStorage
import com.example.arifitna.model.firebase.FirebaseRepository

class GetUserDataUseCase(private val repository: FirebaseRepository) {
    fun execute(): LiveData<UserStorage> {
        return repository.getUserData()
    }
}