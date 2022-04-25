package com.example.arifitna.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.arifitna.model.room.dto.UserData
import com.example.arifitna.model.room.dto.PendingInt

@Dao
interface AppRoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPendingInt(listCurrency: PendingInt)

    @Query("SELECT * FROM alarm_tables")
    suspend fun getAllPendingInt(): List<PendingInt>

    @Query("DELETE FROM alarm_tables")
    suspend fun deleteAllPendingInt()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userData: UserData)

    @Query("SELECT * FROM user_data_tables")
    suspend fun getAllNotes(): UserData
}