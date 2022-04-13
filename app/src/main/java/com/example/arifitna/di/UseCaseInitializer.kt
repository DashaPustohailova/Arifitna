package com.example.arifitna.di

import com.example.arifitna.use_case.*
import org.koin.dsl.module

val useCaseModule = module{
    factory<SavePendingIntUseCase> {
        SavePendingIntUseCase(get())
    }

    factory<GetPendingIntUseCase> {
        GetPendingIntUseCase(get())
    }

    factory<InitDatabaseUseCase> {
        InitDatabaseUseCase(get())
    }

    factory<SignOutUseCase> {
        SignOutUseCase(get())
    }

    factory<CreateReportUseCase> {
        CreateReportUseCase(get())
    }

    factory<InitBaseDataUseCase> {
        InitBaseDataUseCase(get())
    }

    factory<GetCurrentReportUseCase> {
        GetCurrentReportUseCase(get())
    }

    factory<GetLastReportUseCase> {
        GetLastReportUseCase(get())
    }

    factory<UpdateCountWaterUseCase> {
        UpdateCountWaterUseCase(get())
    }
    factory<GetUserDataUseCase> {
        GetUserDataUseCase(get())
    }

    factory<AllReportUseCase> {
        AllReportUseCase(get())
    }
    single<AlarmUseCase> {
        AlarmUseCase(get(),get())
    }
}