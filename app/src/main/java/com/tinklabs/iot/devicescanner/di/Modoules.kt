package com.tinklabs.iot.devicescanner.di

import androidx.room.Room
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.tinklabs.iot.devicescanner.business.MainViewModel
import com.tinklabs.iot.devicescanner.business.batchscan.BatchScanViewModel
import com.tinklabs.iot.devicescanner.business.index.IndexViewModel
import com.tinklabs.iot.devicescanner.db.AppDataBase
import com.tinklabs.iot.devicescanner.http.BaseApiClient
import com.tinklabs.iot.devicescanner.http.HttpApi
import com.tinklabs.iot.devicescanner.utils.HSMDecoderManager
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val appModule: Module = module {

    viewModel {
            params -> BatchScanViewModel(params[0], get(), get())
    }

    viewModel {
        IndexViewModel(get())
    }

    viewModel {
        MainViewModel()
    }

    single {
        HSMDecoderManager(androidContext())
    }

    single {
        BaseApiClient().createService(HttpApi::class.java)
    }

    factory {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //.requestIdToken(androidApplication().getString(R.string.server_client_id))
            .requestEmail()
            .build()
    }
    single {
        Room.databaseBuilder(androidApplication(),
            AppDataBase::class.java, "scanner_db")
            .build()
    }
}