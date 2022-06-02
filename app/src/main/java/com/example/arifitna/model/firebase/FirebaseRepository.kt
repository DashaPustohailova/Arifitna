package com.example.arifitna.model.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.arifitna.model.*
import com.example.arifitna.util.Constants.CURRENT_ID
import com.example.arifitna.util.Constants.ID_REPORT
import com.example.arifitna.util.Constants.REF_DATABASE
import com.example.arifitna.util.Constants.REF_STORAGE_ROOT
import com.example.arifitna.util.Constants.USER_DATA_STORAGE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.security.Permission
import java.text.SimpleDateFormat
import java.util.*


class FirebaseRepository {
    private var AUTH: FirebaseAuth = FirebaseAuth.getInstance()
    val sdf = SimpleDateFormat("dd.M.yyyy")

    fun connectToDatabase(
        inputEmail: String,
        inputPassword: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        AUTH.signInWithEmailAndPassword(inputEmail, inputPassword)
            .addOnSuccessListener {
                CURRENT_ID = AUTH.currentUser?.uid.toString()
                REF_DATABASE = FirebaseDatabase.getInstance().reference
                REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
                onSuccess()
            }
            .addOnFailureListener {
                onFail()
            }
    }

    fun secondAuth() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            CURRENT_ID = user.uid
            REF_DATABASE = FirebaseDatabase.getInstance().reference
            REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
            Log.d("hhh", user.uid + user.email)
        } else {
            Log.d("hhh", "hhh")
        }
    }

    fun signOut() {
        AUTH.signOut()
        CURRENT_ID = ""
        ID_REPORT = ""
        USER_DATA_STORAGE = UserStorage()
    }

    fun registration(
        inputEmail: String,
        inputPassword: String,
        userData: UserStorage,
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        AUTH.createUserWithEmailAndPassword(inputEmail, inputPassword)
            .addOnSuccessListener {
                connectToDatabase(inputEmail, inputPassword,
                    onSuccess = {
                        val userDataNote = hashMapOf<String, Any>()
                        val userPermission = hashMapOf<String, Any>()
                        userPermission["updatePhoto"] = false
                        userPermission["drinks"] = false
                        userDataNote["name"] = userData.name
                        userDataNote["gender"] = userData.gender
                        userDataNote["weight"] = userData.weight
                        if (userData.gender.equals("Woman")) {
                            userDataNote["normWater"] = userData.weight.toInt() * 31
                            Log.d("Value", userData.weight.toString())
                        } else {
                            userDataNote["normWater"] = userData.weight.toInt() * 35
                            Log.d("Value", userData.weight.toString())
                        }
                        userDataNote["bonus"] = userData.bonus

                        REF_DATABASE?.child("users/${AUTH.currentUser?.uid.toString()}")
                            ?.updateChildren(userDataNote)
                            ?.addOnSuccessListener {
                            }

                        REF_DATABASE?.child("permissions/${AUTH.currentUser?.uid.toString()}")
                            ?.updateChildren(userPermission)
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
        val idReport = REF_DATABASE?.ref
            ?.child("report/$CURRENT_ID")
            ?.push()?.key.toString()
        val reportNote = hashMapOf<String, Any>()
        ID_REPORT = idReport
        val report = Report(date = sdf.format(Date()), water = "0")
        reportNote["date"] = report.date
        reportNote["water"] = report.water
        reportNote["allWater"] = report.allWater
        REF_DATABASE?.child("report/$CURRENT_ID/$idReport")
            ?.updateChildren(reportNote)
            ?.addOnSuccessListener {}
    }

    fun initBaseData(current_id: String) {
        CURRENT_ID = current_id
        REF_DATABASE = FirebaseDatabase.getInstance().reference
        secondAuth()
    }

    fun getCurrentDateReport(): LiveData<Long> {
        return CurrentDateReportLiveData()
    }

    fun getLastReport(): LiveData<Report> {
        return LastReportLiveData()
    }

    fun getPermissions() : LiveData<UserPermissions>{
        return PermissionsLiveData()
    }


    fun getPrice(): LiveData<Price>{
        return PriceLiveData()
    }

    fun updatePermissions(permission: String){
        REF_DATABASE?.child("permissions/$CURRENT_ID/$permission")?.setValue(true)
    }

    fun updateBonus(bonus: Int) {
        REF_DATABASE?.child("users/$CURRENT_ID/bonus")?.setValue(bonus)?.addOnSuccessListener {
            Log.d("bonus", "asddddd")
        }
    }

    fun pay(
        bonus: Int,
        price: Int,
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        if (price > bonus) {

            REF_DATABASE?.child("users/$CURRENT_ID/bonus")?.setValue(bonus - price)
                ?.addOnSuccessListener {
                    onSuccess()
                }
                ?.addOnFailureListener {
                    onFail()
                }

        } else {
            onFail()
        }
    }

    fun updatePersonalData(
        userData: UserStorage,
        name: String,
        weight: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit) {
        val userDataNote = hashMapOf<String, Any>()
        userDataNote["name"] = name
        userDataNote["gender"] = userData.gender
        userDataNote["weight"] = weight.toInt()
        if (userData.gender.equals("Woman")) {
            userDataNote["normWater"] = weight.toInt() * 31
            Log.d("Value", userData.weight.toString())
        } else {
            userDataNote["normWater"] = weight.toInt() * 35
            Log.d("Value", userData.weight.toString())
        }
        userDataNote["bonus"] = userData.bonus

        REF_DATABASE?.child("users/${AUTH.currentUser?.uid.toString()}")
            ?.updateChildren(userDataNote)
            ?.addOnSuccessListener {
                onSuccess()
            }
            ?.addOnFailureListener{
                onFail()
            }
    }

    fun updateReport(report: Report) {
        val reportNote = hashMapOf<String, Any>()
        reportNote["date"] = report.date
        reportNote["water"] = report.water
        reportNote["allWater"] = report.allWater

        REF_DATABASE?.child("report/$CURRENT_ID/$ID_REPORT")
            ?.updateChildren(reportNote)
            ?.addOnSuccessListener { }
    }

    fun getUserData(): LiveData<UserStorage> {
        return UserDataLiveData()
    }

    fun getAllReport(): LiveData<List<Report>> {
        return AllReportLiveData()
    }

    fun getAllDrinks(): LiveData<List<Drinks>> {
        return AllDrinksLiveData()
    }
}