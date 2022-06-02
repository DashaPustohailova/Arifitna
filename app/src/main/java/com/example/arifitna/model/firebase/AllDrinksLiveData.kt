package com.example.arifitna.model.firebase

import androidx.lifecycle.LiveData
import com.example.arifitna.model.Drinks
import com.example.arifitna.model.Report
import com.example.arifitna.util.Constants.CURRENT_ID
import com.example.arifitna.util.Constants.REF_DATABASE
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AllDrinksLiveData : LiveData<List<Drinks>>(){
    private val mDatabaseReferenceWater = REF_DATABASE?.ref
        ?.child("drinks")

    private val listener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            value = snapshot.children.map {
                //если не смогли получить запись, то просто возвращаем пустую запись
                it.getValue(Drinks::class.java)?:Drinks()
            }
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

