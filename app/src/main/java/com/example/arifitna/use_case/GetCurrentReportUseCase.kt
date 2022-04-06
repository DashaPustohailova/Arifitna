package com.example.arifitna.use_case

import androidx.lifecycle.LiveData
import com.example.arifitna.model.firebase.FirebaseRepository

class GetCurrentReportUseCase (
    private val repository: FirebaseRepository
) {
    fun execute() : LiveData<Long> {
        return repository.getCurrentDateReport()
    }
}