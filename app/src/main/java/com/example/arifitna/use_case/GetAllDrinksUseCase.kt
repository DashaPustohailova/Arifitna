package com.example.arifitna.use_case

import androidx.lifecycle.LiveData
import com.example.arifitna.model.Drinks
import com.example.arifitna.model.Report
import com.example.arifitna.model.firebase.FirebaseRepository

class GetAllDrinksUseCase(
private val repository: FirebaseRepository
) {
    fun execute(): LiveData<List<Drinks>> {
        return repository.getAllDrinks()
    }
}