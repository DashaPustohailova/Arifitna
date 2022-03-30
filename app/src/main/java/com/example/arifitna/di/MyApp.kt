package com.example.arifitna.di

import android.app.Application
import com.exapmle.focusstart.di.viewModelModule
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class app : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@app)
            modules(listOf(viewModelModule, roomModule,serviseModule, reciverModule, useCaseModule))
        }
    }
}