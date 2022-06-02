package com.example.arifitna.use_case

import androidx.lifecycle.LiveData
import com.example.arifitna.model.Price
import com.example.arifitna.model.firebase.FirebaseRepository

class GetPrice(private val repository: FirebaseRepository) {
    fun execute(): LiveData<Price> {
        return repository.getPrice()
    }
}