package com.example.arifitna.use_case

import androidx.lifecycle.LiveData
import com.example.arifitna.model.UserPermissions
import com.example.arifitna.model.firebase.FirebaseRepository

class GetUserPermission(private val repository: FirebaseRepository) {
    fun execute(): LiveData<UserPermissions> {
        return repository.getPermissions()
    }
}