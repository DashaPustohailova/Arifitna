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

    factory<AddBonusUseCase> {
        AddBonusUseCase(get())
    }

    factory<GetAllDrinksUseCase> {
        GetAllDrinksUseCase(get())
    }

    factory<UpdatePersonalDataUseCase> {
        UpdatePersonalDataUseCase(get())
    }
    factory<GetUserPermission> {
        GetUserPermission(get())
    }

    factory<GetPrice> {
        GetPrice(get())
    }
    factory<UpdatePermissionsUseCase> {
        UpdatePermissionsUseCase(get())
    }

    single<AlarmUseCase> {
        AlarmUseCase(get(),get())
    }
}