package com.example.arifitna.model.firebase

import androidx.lifecycle.LiveData
import com.example.arifitna.model.Price
import com.example.arifitna.util.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class PriceLiveData: LiveData<Price>(){
    private val mDatabaseReferenceWater = Constants.REF_DATABASE?.ref
        ?.child("price")

    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            value = snapshot.getValue(Price::class.java) ?: Price()

        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    //срабатывает, когда LiveData активна. LiveData активна, когда viewModel активна
    //viewModel активна, когда активен фрагмент, который к ней обращается
    override fun onActive() {
        mDatabaseReferenceWater?.addValueEventListener(listener)
        super.onActive()
    }

    override fun onInactive() {
        mDatabaseReferenceWater?.removeEventListener(listener)
        super.onInactive()
    }
}