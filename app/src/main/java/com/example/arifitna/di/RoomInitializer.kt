package com.example.arifitna.di

import com.example.arifitna.model.room.AppRoomDao
import com.example.arifitna.model.room.AppRoomDatabase
import com.example.arifitna.model.room.AppRoomRepository
import org.koin.dsl.module

val roomModule = module {
    single<AppRoomRepository> {
        AppRoomRepository(get())
    }

    single<AppRoomDao> {
        AppRoomDatabase.getInstance(get()).getAppRoomDao()
    }
}