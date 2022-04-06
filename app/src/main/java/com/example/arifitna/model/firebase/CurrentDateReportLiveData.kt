package com.example.arifitna.model.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.arifitna.util.Constants.CURRENT_ID
import com.example.arifitna.util.Constants.REF_DATABASE
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*


class CurrentDateReportLiveData: LiveData<Long>() {
    val sdf = SimpleDateFormat("dd.M.yyyy")
    val currentDate = sdf.format(Date())

    private val mDatabaseReferenceCurrentDateReport = REF_DATABASE?.ref
        ?.child("report")?.child(CURRENT_ID)?.orderByChild("date")?.equalTo(currentDate)


    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            value = snapshot.childrenCount
            Log.d("Value",  "live" + value.toString() + currentDate)
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    //срабатывает, когда LiveData активна. LiveData активна, когда viewModel активна
    //viewModel активна, когда активен фрагмент, который к ней обращается
    override fun onActive() {
        mDatabaseReferenceCurrentDateReport?.addValueEventListener(listener)
        super.onActive()
    }

    override fun onInactive() {
        mDatabaseReferenceCurrentDateReport?.removeEventListener(listener)
        super.onInactive()
    }
}