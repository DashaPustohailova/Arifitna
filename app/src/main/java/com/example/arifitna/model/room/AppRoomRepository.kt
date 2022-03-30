package com.example.arifitna.model.room

import com.example.focusstart.model.room.dto.PendingInt

class AppRoomRepository (private val appRoomDao: AppRoomDao) {

    suspend fun allInt(): List<PendingInt> {
         return  appRoomDao.getAllNotes()
    }

    suspend fun insert(PendingInt: PendingInt) {
        appRoomDao.insert(PendingInt)
    }
    suspend fun delete() {
        appRoomDao.deleteAll()
    }
}