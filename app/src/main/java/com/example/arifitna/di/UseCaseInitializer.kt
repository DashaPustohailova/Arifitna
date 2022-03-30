package com.example.arifitna.di

import com.example.arifitna.use_case.GetPendingIntUseCase
import com.example.arifitna.use_case.SavePendingIntUseCase
import org.koin.dsl.module

val useCaseModule = module{
    factory<SavePendingIntUseCase> {
        SavePendingIntUseCase(get())
    }

    factory<GetPendingIntUseCase> {
        GetPendingIntUseCase(get())
    }
}