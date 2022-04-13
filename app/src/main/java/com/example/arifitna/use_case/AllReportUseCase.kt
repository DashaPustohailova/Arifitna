package com.example.arifitna.use_case

import androidx.lifecycle.LiveData
import com.example.arifitna.model.Report
import com.example.arifitna.model.firebase.FirebaseRepository

class AllReportUseCase(
    private val repository: FirebaseRepository
) {
    fun execute(): LiveData<List<Report>>{
        return repository.getAllReport()
    }
}