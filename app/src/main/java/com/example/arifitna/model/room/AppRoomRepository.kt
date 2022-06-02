package com.example.arifitna.model.room

import com.example.arifitna.model.room.dto.PendingInt

class AppRoomRepository(private val appRoomDao: AppRoomDao) {

    suspend fun allInt(): List<PendingInt> {
        return appRoomDao.getAllPendingInt()
    }

    suspend fun insert(PendingInt: PendingInt) {
        appRoomDao.insertPendingInt(PendingInt)
    }

    suspend fun delete() {
        appRoomDao.deleteAllPendingInt()
    }
}