package com.example.arifitna.use_case

import com.example.arifitna.model.firebase.FirebaseRepository

class SignOutUseCase(
    private val repository: FirebaseRepository
) {
    fun execute(){
        repository.signOut()
    }
}