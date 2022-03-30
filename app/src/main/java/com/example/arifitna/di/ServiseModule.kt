package com.example.arifitna.di

import com.example.arifitna.use_case.AlarmUseCase
import org.koin.dsl.module

val serviseModule  = module{
    single<AlarmUseCase> {
        AlarmUseCase(get(),get())
    }
}