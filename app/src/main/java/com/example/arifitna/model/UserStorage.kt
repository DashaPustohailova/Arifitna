package com.example.arifitna.model

import androidx.room.ColumnInfo

data class UserStorage(
    val name: String = "name",
    val gender: String = "gender",
    val weight: Int = 0,
    val normWater: Int = 0,
    val photoUrl: String = "",
    val bonus: Int = 0,
    val addPhoto:Boolean = false,
    val drinks:Boolean = false
)
