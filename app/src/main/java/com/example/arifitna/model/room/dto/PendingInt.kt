package com.example.arifitna.model.room.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName="alarm_tables")
data class PendingInt(
    @PrimaryKey val id: Int
): Serializable
