package com.example.arifitna.use_case

import com.example.arifitna.model.firebase.FirebaseRepository

class AddBonusUseCase (
    private val repository: FirebaseRepository
){
    fun execute(bonus: Int){
        repository.updateBonus(bonus)
    }
}