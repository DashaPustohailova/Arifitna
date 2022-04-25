package com.example.arifitna.util

import com.example.arifitna.model.UserStorage
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

object Constants {
    //alarm
    const val ACTION_SET_EXACT_ALARM = "ACTION_SET_EXACT_ALARM"
    const val ACTION_SET_REPETITIVE_ALARM = "ACTION_SET_REPETITIVE_ALARM"
    const val EXTRA_EXACT_ALARM_TIME = "EXTRA_EXACT_ALARM_TIME"

    //firebase
    var ID_REPORT:String? = ""
    var CURRENT_ID: String = ""
    var REF_DATABASE: DatabaseReference?= null
    var USER_DATA_STORAGE = UserStorage()
    var REF_STORAGE_ROOT :StorageReference ?= null
}
