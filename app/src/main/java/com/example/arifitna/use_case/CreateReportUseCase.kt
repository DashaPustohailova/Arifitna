package com.example.arifitna.use_case

import com.example.arifitna.model.firebase.FirebaseRepository

class CreateReportUseCase(
    private val repository: FirebaseRepository
) {
    fun execute()  {
        return repository.createReport()
    }
}