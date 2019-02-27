package com.tinklabs.iot.devicescanner.app

import android.app.Application
import com.tinklabs.iot.devicescanner.db.AppDataBase
import com.tinklabs.iot.devicescanner.di.appModule
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class DSApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin(this, listOf(appModule))
    }
}