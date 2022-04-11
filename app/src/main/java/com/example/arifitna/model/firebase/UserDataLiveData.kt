package com.example.arifitna.model.firebase

import androidx.lifecycle.LiveData
import com.example.arifitna.model.UserStorage
import com.example.arifitna.util.Constants.CURRENT_ID
import com.example.arifitna.util.Constants.REF_DATABASE
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class UserDataLiveData: LiveData<UserStorage>() {
    private val mDatabaseReferenceWater = REF_DATABASE?.ref
        ?.child("users")?.child(CURRENT_ID)

    private val listener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            value = snapshot.getValue(UserStorage::class.java) ?: UserStorage()
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