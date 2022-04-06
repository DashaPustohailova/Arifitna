package com.example.arifitna.use_case

import com.example.arifitna.model.firebase.FirebaseRepository

class InitBaseDataUseCase(
    private val repository: FirebaseRepository
) {
    fun execute(current_id: String){
        repository.initBaseData(current_id)
    }

}