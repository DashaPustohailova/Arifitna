package com.example.arifitna.model.room.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName="user_data_tables")
data class UserData (
    @PrimaryKey val id: String,
    @ColumnInfo val name: String = "name",
    @ColumnInfo val gender: String = "gender",
    @ColumnInfo val weight: Int = 0,
    @ColumnInfo val normWater: Int = 0
) : Serializable