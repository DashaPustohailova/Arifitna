package com.example.arifitna.use_case

import com.example.arifitna.model.Report
import com.example.arifitna.model.firebase.FirebaseRepository

class UpdatePermissionsUseCase(
    private val repository: FirebaseRepository
) {
    suspend fun execute(permission: String) {
        repository.updatePermissions(permission)
    }

}