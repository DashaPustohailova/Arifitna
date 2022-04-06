package com.example.arifitna.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.arifitna.model.room.dto.UserData
import com.example.focusstart.model.room.dto.PendingInt

@Database(entities = [PendingInt::class, UserData::class], version = 2, exportSchema = false)
abstract  class AppRoomDatabase: RoomDatabase() {

    abstract fun getAppRoomDao(): AppRoomDao

    companion object {
        @Volatile
        private var database: AppRoomDatabase?=null

        @Synchronized
        fun getInstance(context: Context): AppRoomDatabase {
            return if(database == null){
                database = Room.databaseBuilder(
                    context,
                    AppRoomDatabase::class.java,
                    "database"
                ).build()
                database as AppRoomDatabase
            } else {
                database as AppRoomDatabase
            }
        }
    }
}