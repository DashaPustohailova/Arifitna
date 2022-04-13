package com.example.arifitna.model.firebase

import androidx.lifecycle.LiveData
import com.example.arifitna.model.Report
import com.example.arifitna.util.Constants.CURRENT_ID
import com.example.arifitna.util.Constants.REF_DATABASE
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AllReportLiveData : LiveData<List<Report>>(){
    private val mDatabaseReferenceWater = REF_DATABASE?.ref
        ?.child("report")?.child(CURRENT_ID)

    private val listener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            value = snapshot.children.map {
                //если не смогли получить запись, то просто возвращаем пустую запись
                it.getValue(Report::class.java)?:Report()
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

