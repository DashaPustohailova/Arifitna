package com.example.arifitna.use_case

import com.example.arifitna.model.Report
import com.example.arifitna.model.firebase.FirebaseRepository

class UpdateCountWaterUseCase(
    private val repository: FirebaseRepository
) {
    fun execute(report: Report) {
        repository.updateReport(report = report)
    }
}