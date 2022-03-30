package com.example.arifitna.di

import com.example.arifitna.receiver.AlarmReceiver
import org.koin.dsl.module

val reciverModule = module {
    single {
        AlarmReceiver()
    }
}