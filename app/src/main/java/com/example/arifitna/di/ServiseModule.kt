package com.example.arifitna.di

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.arifitna.service.MyService
import com.example.arifitna.use_case.AlarmUseCase
import org.koin.dsl.module

val serviseModule  = module{
    single<MyService> {
        MyService()
    }

    single<SharedPreferences> {
        PreferenceManager.getDefaultSharedPreferences(get())
    }

   single<SharedPreferences.Editor> {
        PreferenceManager.getDefaultSharedPreferences(get()).edit()
    }

}