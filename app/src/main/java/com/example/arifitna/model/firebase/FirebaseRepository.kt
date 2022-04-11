package com.example.arifitna.model.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.arifitna.model.Report
import com.example.arifitna.model.UserStorage
import com.example.arifitna.util.Constants.CURRENT_ID
import com.example.arifitna.util.Constants.ID_REPORT
import com.example.arifitna.util.Constants.REF_DATABASE
import com.example.arifitna.util.Constants.USER_DATA_STORAGE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class FirebaseRepository {
    private var AUTH: FirebaseAuth  = FirebaseAuth.getInstance()
    val sdf = SimpleDateFormat("dd.M.yyyy")

    fun connectToDatabase(inputEmail: String, inputPassword: String,onSuccess: () -> Unit, onFail: () -> Unit) {
        AUTH.signInWithEmailAndPassword(inputEmail, inputPassword)
            .addOnSuccessListener {
                CURRENT_ID = AUTH.currentUser?.uid.toString()
                REF_DATABASE = FirebaseDatabase.getInstance().reference
                Log.d("Value", "sign in")
                onSuccess()
            }
            .addOnFailureListener {
                onFail()
            }
    }

    fun signOut() {
        AUTH.signOut()
        CURRENT_ID = ""
        ID_REPORT = ""
        USER_DATA_STORAGE = UserStorage()
    }

    fun registration(inputEmail: String, inputPassword: String, userData: UserStorage, onSuccess: () -> Unit, onFail: () -> Unit) {
        AUTH.createUserWithEmailAndPassword(inputEmail, inputPassword)
            .addOnSuccessListener {
                connectToDatabase(inputEmail, inputPassword,
                    onSuccess = {
                        val userDataNote = hashMapOf<String, Any>()
                        userDataNote["name"] = userData.name
                        userDataNote["gender"] = userData.gender
                        userDataNote["weight"] = userData.weight
                        if(userData.gender.equals("Woman")){
                            userDataNote["normWater"] = userData.weight.toInt() * 31
                            Log.d("Value", userData.weight.toString())
                        }
                        else {
                            userDataNote["normWater"] = userData.weight.toInt() * 35
                            Log.d("Value", userData.weight.toString())
                        }

                        REF_DATABASE?.child("users/${AUTH.currentUser?.uid.toString()}")
                            ?.updateChildren(userDataNote)
                            ?.addOnSuccessListener {
                            }
                        onSuccess()
                    },
                    onFail = {
                        onFail()
                    }
                )
           }
    }

    fun createReport() {
        val idReport  = REF_DATABASE?.ref
            ?.child("report/$CURRENT_ID")
            ?.push()?.key.toString()
        val reportNote = hashMapOf<String, Any>()
        ID_REPORT = idReport
        val report = Report(date = sdf.format(Date()), water = "0")
        reportNote["date"] =  report.date
        reportNote["water"] = report.water
        REF_DATABASE?.child("report/$CURRENT_ID/$idReport")
            ?.updateChildren(reportNote)
            ?.addOnSuccessListener {}
    }

    fun initBaseData(current_id: String) {
        CURRENT_ID = current_id
        REF_DATABASE = FirebaseDatabase.getInstance().reference
    }

    fun getCurrentDateReport(): LiveData<Long> {
        return CurrentDateReportLiveData()
    }

    fun getLastReport(): LiveData<Report> {
        return LastReportLiveData()
    }

    fun updateReport(report: Report) {
        val reportNote = hashMapOf<String, Any>()
        reportNote["date"] =  report.date
        reportNote["water"] = report.water

        REF_DATABASE?.child("report/$CURRENT_ID/$ID_REPORT")
            ?.updateChildren(reportNote)
            ?.addOnSuccessListener {  }
    }

    fun getUserData(): LiveData<UserStorage> {
        return UserDataLiveData()
    }
}