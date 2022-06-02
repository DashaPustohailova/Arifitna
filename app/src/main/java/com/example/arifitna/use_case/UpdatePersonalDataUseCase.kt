package com.example.arifitna.use_case

import com.example.arifitna.model.Report
import com.example.arifitna.model.UserStorage
import com.example.arifitna.model.firebase.FirebaseRepository

class UpdatePersonalDataUseCase(
    private val repository: FirebaseRepository
) {
    suspend fun execute(
        userData: UserStorage,
        name: String,
        weight: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        repository.updatePersonalData(
            userData,
            name,
            weight,
            { onSuccess() },
            { onFail() }
        )

    }

}
