package com.example.focusstart.model.room.dto

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName="currency_tables")
data class PendingInt(
    @PrimaryKey val id: Int
): Serializable
