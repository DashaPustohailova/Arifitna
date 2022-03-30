package com.example.arifitna.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.focusstart.model.room.dto.PendingInt

@Dao
interface AppRoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(listCurrency: PendingInt)

    @Query("SELECT * FROM currency_tables")
    suspend fun getAllNotes(): List<PendingInt>

    @Query("DELETE FROM currency_tables")
    suspend fun deleteAll()
}